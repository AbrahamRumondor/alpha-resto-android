package com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.FragmentOrderSummaryBinding
import com.example.alfaresto_customersapp.databinding.OrderPaymentMethodBinding
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.ui.components.listener.OrderSummaryItemListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderSummaryFragment : Fragment() {

    private lateinit var binding: FragmentOrderSummaryBinding
    private val orderSummaryViewModel: OrderSummaryViewModel by activityViewModels()
    private val orderAdapter by lazy { OrderSummaryAdapter() }

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
    }

    private fun populateOrderSummaryAdapter() {
        lifecycleScope.launch {
            orderSummaryViewModel.carts.collectLatest { carts ->
                orderSummaryViewModel.menus.collectLatest { menus ->
                    val orders = carts.mapNotNull { cartOrder ->
                        menus.find { it.menuId == cartOrder.menuId }
                    }
                    countTotalItemAndPrice(menus)
                    orderAdapter.submitOrderList(
                        orderSummaryViewModel.makeOrders(
                            orders = orders,
                            total = countTotalItemAndPrice(menus),
                            address = Address(
                                address = "Jl. Alam Sutera Boulevard No.Kav. 21, Pakulonan, Kec. Serpong Utara, Kota Tangerang Selatan, Banten 15325",
                                addressLabel = "Kantor",
                                addressID = "adfi90sdjaaf98uf",
                                latitude = 0.0,
                                longitude = 0.0
                            )
                        )
                    )

                    binding.rvOrderSummary.adapter = orderAdapter
                    setOrderSummaryListener(orders)
                }
            }
        }
    }

    private fun setOrderSummaryListener(orders: List<Menu>) {
        orderAdapter.setItemListener(object : OrderSummaryItemListener {
            override fun onAddressClicked(position: Int) {
//                TODO go to address page
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_orderSummaryFragment_to_addressList)
            }

            override fun onAddItemClicked(position: Int) {
                val addMenu = orderSummaryViewModel.orders.value[position] as? Menu
                addMenu?.let {
                    it.orderCartQuantity += 1
                    orderAdapter.notifyItemChanged(position)
                    orderAdapter.notifyItemChanged(orderSummaryViewModel.orders.value.size - 3)
                    countTotalItemAndPrice(orders)
                }
            }

            override fun onDecreaseItemClicked(position: Int) {
                val addMenu = orderSummaryViewModel.orders.value[position] as? Menu
                addMenu?.let {
                    if (it.orderCartQuantity > 0) {
                        it.orderCartQuantity -= 1
                        if (it.orderCartQuantity == 0) {
                            removeMenu(position)
                        } else {
                            orderAdapter.notifyItemChanged(position)
                        }
                    }
                    countTotalItemAndPrice(orders)
                    orderAdapter.notifyItemChanged(orderSummaryViewModel.orders.value.size - 3)
                }
            }

            override fun onDeleteItemClicked(position: Int) {
                removeMenu(position)
            }

            override fun onRadioButtonClicked(position: Int, id: Int) {
                val payment = if (id == R.id.rb_cod) {
                    "COD"
                } else {
                    "GOPAY"
                }
                orderSummaryViewModel.setPayment(payment)
            }

            override fun onPaymentMethodClicked(view: OrderPaymentMethodBinding) {
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

            override fun onCheckoutButtonClicked() {
                // TODO send to firebase

            }

        })
    }

    private fun removeMenu(position: Int) {
        val size = orderSummaryViewModel.removeOrder(position)
        orderAdapter.notifyItemRemoved(position)
        orderAdapter.notifyItemRangeChanged(position, size)
    }

    private fun countTotalItemAndPrice(list: List<Menu>): Pair<Int, Int> {
        var totalPrice = 0
        var totalItem = 0
        list.forEach {
            totalPrice += it.menuPrice * it.orderCartQuantity
            totalItem += it.orderCartQuantity
        }
        if (totalPrice == 0 && totalItem == 0) {
            findNavController().popBackStack()
        }
        return Pair(totalItem, totalPrice)
    }
}