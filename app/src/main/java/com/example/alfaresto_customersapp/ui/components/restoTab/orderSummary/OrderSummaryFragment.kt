package com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.FragmentOrderSummaryBinding
import com.example.alfaresto_customersapp.databinding.FragmentRestoBinding
import com.example.alfaresto_customersapp.databinding.OrderPaymentMethodBinding
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.ui.components.listener.OrderSummaryItemListener

class OrderSummaryFragment : Fragment() {

    private lateinit var binding: FragmentOrderSummaryBinding
    private val orderSummaryViewModel: OrderSummaryViewModel by activityViewModels()
    private lateinit var orderAdapter: OrderSummaryAdapter

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
        countTotalItemAndPrice()
        orderAdapter = OrderSummaryAdapter(orderSummaryViewModel.cart)
        binding.rvOrderSummary.adapter = orderAdapter
        setOrderSummaryListener()
    }

    private fun setOrderSummaryListener() {
        orderAdapter.setItemListener(object : OrderSummaryItemListener {
            override fun onAddressClicked(position: Int) {
//                TODO go to address page
                Navigation.findNavController(binding.root).navigate(R.id.action_orderSummaryFragment_to_addressList)
            }

            override fun onAddItemClicked(position: Int) {
                val addMenu = orderSummaryViewModel.cart[position] as? Menu
                addMenu?.let {
                    it.orderCartQuantity += 1
                    orderAdapter.notifyItemChanged(position)
                    orderAdapter.notifyItemChanged(orderSummaryViewModel.cart.size - 3)
                    countTotalItemAndPrice()
                }
            }

            override fun onDecreaseItemClicked(position: Int) {
                val addMenu = orderSummaryViewModel.cart[position] as? Menu
                addMenu?.let {
                    if (it.orderCartQuantity > 0) {
                        it.orderCartQuantity -= 1
                        if (it.orderCartQuantity == 0) {
                            removeMenu(position)
                        } else {
                            orderAdapter.notifyItemChanged(position)
                        }
                    }
                    countTotalItemAndPrice()
                    orderAdapter.notifyItemChanged(orderSummaryViewModel.cart.size - 3)
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
                orderSummaryViewModel.cart[orderSummaryViewModel.cart.size - 2] = payment
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
        orderSummaryViewModel.cart.removeAt(position)
        orderAdapter.notifyItemRemoved(position)
        orderAdapter.notifyItemRangeChanged(position, orderSummaryViewModel.cart.size)
    }

    private fun countTotalItemAndPrice() {
        var totalPrice = 0
        var totalItem = 0
        orderSummaryViewModel.cart.forEach {
            if (it is Menu) {
                totalPrice += it.menuPrice * it.orderCartQuantity
                totalItem += it.orderCartQuantity
            }
        }
        if (totalPrice == 0 && totalItem == 0) {
            findNavController().popBackStack()
        }
        val newPair = Pair(totalItem, totalPrice)
        orderSummaryViewModel.cart[orderSummaryViewModel.cart.size - 3] = newPair
    }
}