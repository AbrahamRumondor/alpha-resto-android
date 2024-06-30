package com.example.alfaresto_customersapp.ui.components.orderHistoryTab

import android.os.Bundle
import android.util.Log
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
import com.example.alfaresto_customersapp.ui.components.listener.OrderHistoryListener
import com.example.alfaresto_customersapp.ui.components.orderHistoryTab.adapter.OrderHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint
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

        lifecycleScope.launch {
            viewModel.isLoading.collectLatest { isLoading ->
                binding.loadingLayout.progressBar.visibility =
                    if (isLoading) View.VISIBLE else View.GONE

                if (!isLoading) {
                    Toast.makeText(requireContext(), "Order History Loaded. There's no order history.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.orderHistories.observe(viewLifecycleOwner) { orderHistories ->
                if (orderHistories.isEmpty()) {
                    Log.d("OrderHistory", "Order History is empty, waiting for data...")
                    // Optionally, you can show a loading state or handle the empty case
                    return@observe
                }

                adapter.submitList(orderHistories)
                binding.rvOrderHistory.adapter = adapter
                binding.rvOrderHistory.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                setOnOrderClickListener()
            }
        }
    }

    private fun setOnOrderClickListener() {
        adapter.setItemListener(object : OrderHistoryListener {
            override fun onOrderClicked(orderHistory: OrderHistory) {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_order_history_fragment_to_track_order_fragment)
            }
        })
    }

}