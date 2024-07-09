package com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.databinding.OrderSummaryAddressBinding
import com.example.alfaresto_customersapp.databinding.OrderSummaryCheckoutButtonBinding
import com.example.alfaresto_customersapp.databinding.OrderSummaryItemBinding
import com.example.alfaresto_customersapp.databinding.OrderSummaryPaymentMethodBinding
import com.example.alfaresto_customersapp.databinding.OrderSummaryTotalPriceBinding
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.model.OrderItem
import com.example.alfaresto_customersapp.ui.components.listener.OrderSummaryItemListener
import com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary.viewHolder.CheckoutButtonViewHolder
import com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary.viewHolder.OrderAddressViewHolder
import com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary.viewHolder.OrderListViewHolder
import com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary.viewHolder.OrderTotalViewHolder
import com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary.viewHolder.PaymentMethodViewHolder

class OrderSummaryAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: MutableList<Any?> = mutableListOf()
    private var orderSummaryItemListener: OrderSummaryItemListener? = null

    private val SHOW_ADDRESS = 0
    private val SHOW_ORDER_LIST = 1
    private val SHOW_ORDER_TOTAL = 2
    private val SHOW_PAYMENT_METHOD = 3
    private val SHOW_CHECKOUT_BUTTON = 4

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Address? -> SHOW_ADDRESS
            is Menu -> SHOW_ORDER_LIST
            is Pair<*, *> -> SHOW_ORDER_TOTAL
            is String -> when (items[position] as String) {
                "payment_method" -> SHOW_PAYMENT_METHOD
                "checkout" -> SHOW_CHECKOUT_BUTTON
                else -> throw IllegalArgumentException("Invalid string type")
            }

            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val bindingAddress = OrderSummaryAddressBinding.inflate(inflater, parent, false)
        val bindingOrderList = OrderSummaryItemBinding.inflate(inflater, parent, false)
        val bindingOrderTotal = OrderSummaryTotalPriceBinding.inflate(inflater, parent, false)
        val bindingPaymentMethod = OrderSummaryPaymentMethodBinding.inflate(inflater, parent, false)
        val bindingCheckoutButton = OrderSummaryCheckoutButtonBinding.inflate(inflater, parent, false)

        return when (viewType) {
            SHOW_ADDRESS -> OrderAddressViewHolder(
                bindingAddress,
                orderSummaryItemListener,
                viewType
            )

            SHOW_ORDER_LIST -> OrderListViewHolder(bindingOrderList, orderSummaryItemListener)
            SHOW_ORDER_TOTAL -> OrderTotalViewHolder(bindingOrderTotal)
            SHOW_PAYMENT_METHOD -> PaymentMethodViewHolder(
                bindingPaymentMethod,
                orderSummaryItemListener,
                viewType
            )

            SHOW_CHECKOUT_BUTTON -> CheckoutButtonViewHolder(
                bindingCheckoutButton,
                orderSummaryItemListener
            )

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is Address? -> (holder as OrderAddressViewHolder).bind(item)
            is Menu -> (holder as OrderListViewHolder).bind(item, position)
            is Pair<*, *> -> (holder as OrderTotalViewHolder).bind(item as Pair<Int, Int>)
            is String -> when (item) {
                "payment_method" -> (holder as PaymentMethodViewHolder).bind()
                "checkout" -> (holder as CheckoutButtonViewHolder).bind()
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitOrderList(orderItems: MutableList<Any?>) {
        this.items.clear()
        this.items.addAll(orderItems)
    }

    fun setItemListener(orderSummaryItemListener: OrderSummaryItemListener) {
        this.orderSummaryItemListener = orderSummaryItemListener
    }
}