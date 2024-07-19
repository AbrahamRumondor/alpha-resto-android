package com.example.alfaresto_customersapp.ui.components.orderHistoryDetailPage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.OrderHistoryDetailBinding
import com.example.alfaresto_customersapp.data.network.NetworkUtils
import com.example.alfaresto_customersapp.ui.components.mainActivity.MainActivity.Companion.CANCELLED
import com.example.alfaresto_customersapp.ui.components.mainActivity.MainActivity.Companion.DELIVERED
import com.example.alfaresto_customersapp.ui.components.mainActivity.MainActivity.Companion.ON_DELIVERY
import com.example.alfaresto_customersapp.ui.components.orderHistoryDetailPage.adapter.OrderHistoryDetailItemsAdapter
import com.example.alfaresto_customersapp.utils.singleton.UserInfo
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
        chatButtonSetup()
        loadData()
        complainButtonHandle()
        onStatusChangeToDelivery()
        setConnectionBehaviour()

        binding.inclInternet.btnInetTryAgain.setOnClickListener {
            setConnectionBehaviour()
        }
    }

    private fun setConnectionBehaviour() {
        if (NetworkUtils.isConnectedToNetwork.value == false) {
            binding.inclInternet.root.visibility = View.VISIBLE
            binding.clBase.visibility = View.GONE
            Toast.makeText(requireContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT)
                .show()
        } else {
            binding.inclInternet.root.visibility = View.GONE
            binding.clBase.visibility = View.VISIBLE
        }
    }

    private fun setupView() {
        binding.toolbar.apply {
            btnLogout.visibility = View.GONE
            btnBack.visibility = View.VISIBLE
            ivToolbarTitle.visibility = View.GONE
            tvToolbarText.visibility = View.VISIBLE
            tvToolbarText.text = args.orderStatus
        }

        binding.rvOrderItems.let {
            it.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            it.adapter = adapter
        }
    }

    private fun setupBackBtn() {
        binding.toolbar.btnBack.setOnClickListener {
            if (
                findNavController().popBackStack(R.id.order_history_fragment, false)
            ) {
                return@setOnClickListener
            } else {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_order_history_detail_fragment_to_order_history_fragment)
            }
        }
    }

    private fun onEmbeddedBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (
                    findNavController().popBackStack(R.id.order_history_fragment, false)
                ) {
                    return
                } else {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_order_history_detail_fragment_to_order_history_fragment)
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun chatButtonSetup() {
        binding.btnChat.setOnClickListener {
            val action =
                OrderHistoryDetailFragmentDirections.actionOrderHistoryDetailFragmentToChatFragment(
                    args.orderId
                )
            Navigation.findNavController(binding.root).navigate(action)
        }
    }

    private fun complainButtonHandle() {
        val statusOrder = args.orderStatus
        val orderId = args.orderId
        binding.apply {
            if (statusOrder == DELIVERED) {
                tvComplain.visibility = View.VISIBLE
                btnComplain.visibility = View.VISIBLE
                btnComplain.setOnClickListener {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data =
                            Uri.parse("mailto:")
                        putExtra(
                            Intent.EXTRA_EMAIL,
                            arrayOf("kelshairaa@gmail.com")
                        )
                        putExtra(
                            Intent.EXTRA_SUBJECT,
                            "Keluhan tentang Pesanan. Order ID: #$orderId"
                        )
                    }
                    startActivity(intent)
                }
            }
        }
    }

    private fun loadData() {
//        lifecycleScope.launch {
//            viewModel.isLoading.collect { isLoading ->
//                if (isLoading) {
//                    binding.shimmerViewContainer.visibility = View.VISIBLE
//                    binding.shimmerViewContainer.startShimmer()
//                    binding.clBase.visibility = View.GONE
//                } else {
//                    binding.shimmerViewContainer.stopShimmer()
//                    binding.shimmerViewContainer.visibility = View.GONE
//                    binding.clBase.visibility = View.VISIBLE
//                }
//            }
//        }
        lifecycleScope.launch {
            viewModel.orderHistory.collectLatest { orderHistory ->
                delay(500)
                binding.apply {
                    tvOrderId.text = orderHistory.orderId
                    tvOrderDate.text = orderHistory.formattedDate()
                    tvTotalPrice.text = String.format("Rp %,d", orderHistory.orderTotalPrice)
                    tvUserAddress.text = orderHistory.addressLabel
                    tvNotesText.text = orderHistory.orderNotes
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
        UserInfo.SHIPMENT.observe(viewLifecycleOwner) {
            if (it.orderID == args.orderId) {
                if (it.statusDelivery == ON_DELIVERY) {
                    val action =
                        OrderHistoryDetailFragmentDirections.actionOrderHistoryDetailFragmentToTrackOrderFragment(
                            orderId = args.orderId,
//                        shipmentId = it
                        )
                    Navigation.findNavController(binding.root)
                        .navigate(action)
                } else if (it.statusDelivery == CANCELLED) {
                    binding.toolbar.tvToolbarText.text = getString(R.string.canceled)
                }
            }
        }
    }
}