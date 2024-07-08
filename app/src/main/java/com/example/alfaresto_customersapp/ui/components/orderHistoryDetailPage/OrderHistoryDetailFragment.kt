package com.example.alfaresto_customersapp.ui.components.orderHistoryDetailPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.OrderHistoryDetailBinding
import com.example.alfaresto_customersapp.domain.model.OrderStatus
import com.example.alfaresto_customersapp.ui.components.orderHistoryDetailPage.adapter.OrderHistoryDetailItemsAdapter
import com.example.alfaresto_customersapp.utils.user.UserConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
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
        viewModel.fetchOrderHistory(args.orderId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupBackBtn()
        onEmbeddedBackPressed()
        loadData()
        onStatusChangeToDelivery()
    }

    private fun setupView() {
        binding.toolbar.apply {
            btnLogout.visibility = View.GONE
            btnBack.visibility = View.VISIBLE
            ivToolbarTitle.visibility = View.GONE
            tvToolbarText.visibility = View.VISIBLE
            tvToolbarText.text = getString(R.string.order_detail)
        }

        binding.rvOrderItems.let {
            it.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            it.adapter = adapter
        }
    }

    private fun setupBackBtn() {
        binding.toolbar.btnBack.setOnClickListener {
            findNavController().popBackStack(R.id.order_history_fragment, false)
        }
    }

    private fun onEmbeddedBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.order_history_fragment, false)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.orderHistory.collectLatest { orderHistory ->
                delay(500)
                binding.apply {
                    tvOrderId.text = orderHistory.orderId
                    tvOrderDate.text = orderHistory.orderDate
                    tvTotalPrice.text = String.format("Rp %,d", orderHistory.orderTotalPrice)
                    tvUserAddress.text = orderHistory.addressLabel
                    tvStatus.text = args.orderStatus
                    orderHistory.orderStatus.name.let { status ->
                        val colorRes = when (status) {
                            OrderStatus.DELIVERED.name -> R.drawable.delivered_shape
                            OrderStatus.ON_DELIVERY.name -> R.drawable.on_delivery_shape
                            else -> R.drawable.on_process_shape
                        }
                        tvStatus.background = AppCompatResources.getDrawable(root.context, colorRes)
                    }
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

    private fun onStatusChangeToDelivery() {
        UserConstants.SHIPMENT_STATUS.observe(viewLifecycleOwner) {
            if (it == "On Delivery") {
                val action =
                    OrderHistoryDetailFragmentDirections.actionOrderHistoryDetailFragmentToTrackOrderFragment(
                        orderId = args.orderId,
                        shipmentId = it
                    )
                Navigation.findNavController(binding.root)
                    .navigate(action)
            }
        }
    }
}