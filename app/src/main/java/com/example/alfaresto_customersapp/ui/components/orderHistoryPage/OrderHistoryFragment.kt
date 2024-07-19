package com.example.alfaresto_customersapp.ui.components.orderHistoryPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.FragmentOrderHistoryBinding
import com.example.alfaresto_customersapp.domain.model.OrderHistory
import com.example.alfaresto_customersapp.domain.model.OrderStatus
import com.example.alfaresto_customersapp.data.network.NetworkUtils
import com.example.alfaresto_customersapp.ui.components.listener.OrderHistoryListener
import com.example.alfaresto_customersapp.ui.components.mainActivity.MainActivity.Companion.DELIVERED
import com.example.alfaresto_customersapp.ui.components.mainActivity.MainActivity.Companion.ON_PROCESS
import com.example.alfaresto_customersapp.ui.components.orderHistoryPage.adapter.OrderHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderHistoryFragment : Fragment() {

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

        toolbarSetup()

        binding.rvOrderHistory.adapter = adapter
        binding.rvOrderHistory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        lifecycleScope.launch {
            viewModel.orderHistories.collectLatest { orderHistories ->
                binding.llNoOrders.visibility = View.GONE

                binding.toolbar.toggleSort.isChecked = true
                adapter.submitList(orderHistories.sortedByDescending { it.orderDate })
                updateToggleButtonIcon(true)

                binding.toolbar.toggleSort.setOnCheckedChangeListener { _, isChecked ->
                    val sortedOrderHistories = if (isChecked) {
                        orderHistories.sortedByDescending { it.orderDate }
                    } else {
                        orderHistories.sortedBy { it.orderDate }
                    }
                    adapter.submitList(sortedOrderHistories)
                    updateToggleButtonIcon(isChecked)
                }

                orderHistories.map {
                    setOnOrderClickListener()
                }

                delay(1000)
                if (orderHistories.isEmpty()) {
                    binding.llNoOrders.visibility = View.VISIBLE
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.no_order_history),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@collectLatest
                }
            }
        }
    }

    private fun toolbarSetup() {
        binding.toolbar.apply {
            tvToolbarText.visibility = View.VISIBLE
            tvToolbarText.text = getString(R.string.order_history)
            btnLogout.visibility = View.GONE
            ivToolbarTitle.visibility = View.GONE
            toggleSort.visibility = View.VISIBLE
            toggleSortLayout.visibility = View.VISIBLE
        }
    }

    private fun updateToggleButtonIcon(isChecked: Boolean) {
        val iconRes = if (isChecked) R.drawable.ic_sort_latest else R.drawable.ic_sort_oldest
        binding.toolbar.toggleSort.setCompoundDrawablesWithIntrinsicBounds(0, iconRes, 0, 0)
    }

    private fun setOnOrderClickListener() {
        adapter.setItemListener(object : OrderHistoryListener {
            override fun onOrderClicked(orderHistory: OrderHistory) {
                if (NetworkUtils.isConnectedToNetwork.value == false) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.no_internet),
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val action = when (orderHistory.orderStatus) {
                    OrderStatus.ON_DELIVERY -> {
                        OrderHistoryFragmentDirections.actionOrderHistoryFragmentToTrackOrderFragment(
                            orderId = orderHistory.orderId,
//                            shipmentId = orderHistory.id
                        )
                    }

                    OrderStatus.ON_PROCESS -> {
                        OrderHistoryFragmentDirections.actionOrderHistoryFragmentToOrderHistoryDetailFragment(
                            orderId = orderHistory.orderId,
                            orderStatus = ON_PROCESS
                        )
                    }

                    else -> {
                        OrderHistoryFragmentDirections.actionOrderHistoryFragmentToOrderHistoryDetailFragment(
                            orderId = orderHistory.orderId,
                            orderStatus = DELIVERED
                        )
                    }
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