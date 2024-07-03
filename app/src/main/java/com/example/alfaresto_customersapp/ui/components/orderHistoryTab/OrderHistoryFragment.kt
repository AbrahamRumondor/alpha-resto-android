package com.example.alfaresto_customersapp.ui.components.orderHistoryTab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.FragmentOrderHistoryBinding
import com.example.alfaresto_customersapp.domain.model.OrderHistory
import com.example.alfaresto_customersapp.domain.model.OrderStatus
import com.example.alfaresto_customersapp.ui.base.BaseFragment
import com.example.alfaresto_customersapp.ui.components.listener.OrderHistoryListener
import com.example.alfaresto_customersapp.ui.components.orderHistoryTab.adapter.OrderHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderHistoryFragment : BaseFragment() {

    private lateinit var binding: FragmentOrderHistoryBinding
    private val viewModel: OrderHistoryViewModel by viewModels()
    private val adapter by lazy { OrderHistoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvOrderHistory.adapter = adapter
        binding.rvOrderHistory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        lifecycleScope.launch {
            viewModel.orderHistories.observe(viewLifecycleOwner) { orderHistories ->
                if (orderHistories.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Order History Loaded. There's no order history.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@observe
                }

                adapter.submitList(orderHistories)
                binding.run {
                    var sortedOrderHistories: List<OrderHistory> =
                        orderHistories.sortedByDescending {
                            it.orderDate
                        } // default is latest

                    btnLatest.setOnClickListener {
                        sortedOrderHistories = orderHistories.sortedByDescending {
                            it.orderDate
                        }
                        btnLatest.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.button_radius)
                        btnLatest.alpha = 1.0F
                        btnOldest.background = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.button_radius_gray
                        )
                        btnOldest.alpha = 0.5F
                        adapter.submitList(sortedOrderHistories)
                    }
                    btnOldest.setOnClickListener {
                        sortedOrderHistories = orderHistories.sortedBy {
                            it.orderDate
                        }
                        btnOldest.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.button_radius)
                        btnOldest.alpha = 1.0F
                        btnLatest.background = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.button_radius_gray
                        )
                        btnLatest.alpha = 0.5F
                        adapter.submitList(sortedOrderHistories)
                    }

                    adapter.submitList(sortedOrderHistories)
                    rvOrderHistory.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//                setOnOrderClickListener()
                }
                orderHistories.map {
                    setOnOrderClickListener()
                }
            }
        }
    }

    private fun setOnOrderClickListener() {
        adapter.setItemListener(object : OrderHistoryListener {
            override fun onOrderClicked(orderHistory: OrderHistory) {
                val action = if (orderHistory.orderStatus == OrderStatus.DELIVERED) {
                    OrderHistoryFragmentDirections.actionOrderHistoryFragmentToOrderHistoryDetailFragment(
                        orderId = orderHistory.orderId
                    )
                } else {
                    OrderHistoryFragmentDirections.actionOrderHistoryFragmentToTrackOrderFragment(
                        orderId = orderHistory.orderId,
                        shipmentId = orderHistory.id
                    )
                }

                Navigation.findNavController(binding.root)
                    .navigate(action)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.setLoadingTrue()
    }
}