package com.example.alfaresto_customersapp.ui.components.orderSummaryPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import com.example.alfaresto_customersapp.databinding.FragmentOrderSummaryBinding
import com.example.alfaresto_customersapp.databinding.OrderSummaryPaymentMethodBinding
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.data.network.NetworkUtils
import com.example.alfaresto_customersapp.ui.components.listener.OrderSummaryItemListener
import com.example.alfaresto_customersapp.utils.singleton.UserInfo.USER_ADDRESS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderSummaryFragment : Fragment() {

    private lateinit var binding: FragmentOrderSummaryBinding
    private val orderSummaryViewModel: OrderSummaryViewModel by activityViewModels()
    private val orderAdapter by lazy { OrderSummaryAdapter() }

    private var checkoutClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateOrderSummaryAdapter()

        setConnectionBehaviour()
        binding.inclInternet.btnInetTryAgain.setOnClickListener {
            setConnectionBehaviour()
        }
    }

    private fun setConnectionBehaviour() {
        if (NetworkUtils.isConnectedToNetwork.value == false) {
            binding.inclInternet.root.visibility = View.VISIBLE
            binding.rvOrderSummary.visibility = View.GONE
            Toast.makeText(requireContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        } else {
            binding.inclInternet.root.visibility = View.GONE
            binding.rvOrderSummary.visibility = View.VISIBLE
        }
    }

    private fun populateOrderSummaryAdapter() {
        lifecycleScope.launch {
            orderSummaryViewModel.carts.collectLatest { carts ->
                orderSummaryViewModel.menus.collectLatest { menus ->
//                    delay(500)
                    val orders = carts.mapNotNull { cartOrder ->
                        menus.find {
                            it.id == cartOrder.menuId && cartOrder.menuQty > 0
                        }?.copy(orderCartQuantity = cartOrder.menuQty)
                    }
                    binding.rvOrderSummary.adapter = orderAdapter
                    orderAdapter.submitOrderList(
                        orderSummaryViewModel.makeOrders(
                            orders = orders,
                            total = countTotalItemAndPrice(orders),
                            address = USER_ADDRESS
                        )
                    )
                    setOrderSummaryListener(orders, carts)
                }
            }
        }
    }

    private fun setOrderSummaryListener(orders: List<Menu>, cart: List<CartEntity>?) {
        orderAdapter.setItemListener(object : OrderSummaryItemListener {
            override fun onAddressClicked(position: Int) {
//                TODO go to address page
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_order_summary_fragment_to_address_list)
            }

            override fun onAddItemClicked(position: Int, menuId: String) {
                if (!noInternetConnection()) {
                    val addMenu = orderSummaryViewModel.orders.value[position] as? Menu
                    addMenu?.let { menu ->
                        val item = cart?.find { it.menuId == menuId }
                        orderSummaryViewModel.addOrderQuantity(menuId, item)
                        menu.orderCartQuantity += 1
                        orderAdapter.notifyItemChanged(position)
                        orderAdapter.notifyItemChanged(orderSummaryViewModel.orders.value.size - 3)
                        countTotalItemAndPrice(orders)
                    }
                }
            }

            override fun onDecreaseItemClicked(position: Int, menuId: String) {
                if (!noInternetConnection()) {
                    val addMenu = orderSummaryViewModel.orders.value[position] as? Menu
                    addMenu?.let {
                        if (it.orderCartQuantity > 0) {
                            it.orderCartQuantity -= 1
                            val item: CartEntity? = cart?.find { it.menuId == menuId }
                            orderSummaryViewModel.decreaseOrderQuantity(menuId, item)
                            if (it.orderCartQuantity == 0) {
                                removeMenu(position, menuId)
                            } else {
                                orderAdapter.notifyItemChanged(position)
                            }
                        }
                        countTotalItemAndPrice(orders)
                        orderAdapter.notifyItemChanged(orderSummaryViewModel.orders.value.size - 3)
                    }
                }
            }

            override fun onDeleteItemClicked(position: Int, menuId: String) {
                if (!noInternetConnection()) {
                    removeMenu(position, menuId)
                }
            }

            override fun onRadioButtonClicked(position: Int, id: Int) {
                val payment = if (id == R.id.rb_cod) {
                    COD
                } else {
                    GOPAY
                }
                orderSummaryViewModel.setPayment(payment)
            }

            override fun onPaymentMethodClicked(view: OrderSummaryPaymentMethodBinding) {
                view.run {
                    if (rgPaymentMethod.visibility == View.GONE) {
                        ivIconAction.setImageResource(R.drawable.ic_down)
                        rgPaymentMethod.visibility = View.VISIBLE
                    } else {
                        ivIconAction.setImageResource(R.drawable.ic_proceed)
                        rgPaymentMethod.visibility = View.GONE
                    }
                }
            }

            override fun onNotesFilled(notes: String) {
                orderSummaryViewModel.setNotes(notes)
            }

            override fun onCheckoutButtonClicked() {
                if (!noInternetConnection()) {
                    val currentTime = orderSummaryViewModel.getCurrentTime()
                    var isClosed = false

                    orderSummaryViewModel.isRestoClosed(currentTime) {
                        isClosed = it

                        if (isClosed) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.restaurant_closed),
                                Toast.LENGTH_SHORT
                            ).show()
                            return@isRestoClosed
                        }

                        if (!checkoutClicked) {
                            checkoutClicked = true
                            AlertDialog.Builder(requireContext())
                                .setTitle(getString(R.string.checkout_confirmation))
                                .setMessage(getString(R.string.checkout_confirmation_message))
                                .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                                    checkoutClicked = false
                                    dialog.dismiss()
                                }
                                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                                    checkout()
                                }
                                .setOnDismissListener {
                                    checkoutClicked = false
                                }
                                .show()
                        }
                    }
                }
            }
        })
    }

    fun noInternetConnection(): Boolean {
        return if (NetworkUtils.isConnectedToNetwork.value == false) {
            Toast.makeText(
                requireContext(),
                getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
            true
        } else {
            false
        }
    }

    private fun checkout() {
        orderSummaryViewModel.saveOrderInDatabase {
            if (it != null) {
                checkoutClicked = false
                Toast.makeText(
                    requireContext(),
                    getString(it),
                    Toast.LENGTH_SHORT
                ).show()
            }

            val action =
                OrderSummaryFragmentDirections.actionOrderSummaryFragmentToThankYouFragment(
                    it == null
                )
            Navigation.findNavController(requireView()).navigate(action)
        }
    }

    private fun removeMenu(position: Int, menuId: String) {
        val size = orderSummaryViewModel.removeOrder(position, menuId)
        orderAdapter.notifyItemRemoved(position)
        orderAdapter.notifyItemRangeChanged(position, size)
    }

    private fun countTotalItemAndPrice(list: List<Menu>): Pair<Int, Int> {
        var totalPrice = 0
        var totalItem = 0
        list.forEach {
            totalPrice += it.price * it.orderCartQuantity
            totalItem += it.orderCartQuantity
        }
//        if (totalPrice == 0 && totalItem == 0) {
//            findNavController().popBackStack()
//        }
        return Pair(totalItem, totalPrice)
    }

    companion object {
        const val COD = "COD"
        const val GOPAY = "GOPAY"
    }
}