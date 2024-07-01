package com.example.alfaresto_customersapp.ui.components.orderHistoryDetailPage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alfaresto_customersapp.databinding.OrderHistoryDetailBinding
import com.example.alfaresto_customersapp.ui.components.orderHistoryDetailPage.adapter.OrderHistoryDetailItemsAdapter
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderHistoryDetailFragment : Fragment() {

    private lateinit var binding: OrderHistoryDetailBinding
    private val viewModel: OrderHistoryDetailViewModel by viewModels()
    private val adapter by lazy { OrderHistoryDetailItemsAdapter() }
    private val args: OrderHistoryDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OrderHistoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchOrderHistory(args.orderId)

        setupView()
        setupBackBtn()
        loadData()
    }

    private fun setupView() {
        binding.rvOrderItems.let {
            it.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            it.adapter = adapter
        }
    }

    private fun setupBackBtn() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.orderHistory.collectLatest { orderHistory ->
                binding.apply {
                    tvOrderId.text = orderHistory.orderId
                    tvOrderDate.text = orderHistory.orderDate
                    tvTotalPrice.text = String.format("Rp %,d", orderHistory.orderTotalPrice)
                    tvUserAddress.text = orderHistory.addressLabel
                    tvOrderStatus.text = orderHistory.orderStatus.toString()
                }

                viewModel.fetchOrderItems(orderHistory.orderId)

                viewModel.orderItems.collectLatest { orderItems ->
                    adapter.submitOrderHistoryDetailItemsList(orderItems)

                    viewModel.user.collectLatest { user ->
                        binding.apply {
                            tvUserName.text = user.name
                            tvUserPhone.text = user.phone
                        }
                    }
                }
            }
        }
    }
}