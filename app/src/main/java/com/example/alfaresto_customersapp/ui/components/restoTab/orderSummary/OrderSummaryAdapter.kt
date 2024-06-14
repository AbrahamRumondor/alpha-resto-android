package com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.databinding.CheckoutButtonBinding
import com.example.alfaresto_customersapp.databinding.OrderAddressBinding
import com.example.alfaresto_customersapp.databinding.OrderItemBinding
import com.example.alfaresto_customersapp.databinding.OrderPaymentMethodBinding
import com.example.alfaresto_customersapp.databinding.OrderTotalPriceBinding
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.ui.components.listener.ItemListener
import com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary.viewHolder.CheckoutButtonViewHolder
import com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary.viewHolder.OrderAddressViewHolder
import com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary.viewHolder.OrderListViewHolder
import com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary.viewHolder.OrderTotalViewHolder
import com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary.viewHolder.PaymentMethodViewHolder

class OrderSummaryAdapter (private val items: List<Any>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemListener: ItemListener? = null

    private val SHOW_ADDRESS = 0
    private val SHOW_ORDER_LIST = 1
    private val SHOW_ORDER_TOTAL = items.size - 3
    private val SHOW_PAYMENT_METHOD = items.size - 2
    private val SHOW_CHECKOUT_BUTTON = items.size - 1

    override fun getItemViewType(position: Int): Int {
        return if(position in SHOW_ORDER_LIST..<SHOW_ORDER_TOTAL) SHOW_ORDER_LIST else position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val bindingAddress = OrderAddressBinding.inflate(inflater, parent, false)
        val bindingOrderList = OrderItemBinding.inflate(inflater, parent, false)
        val bindingOrderTotal = OrderTotalPriceBinding.inflate(inflater, parent, false)
        val bindingPaymentMethod = OrderPaymentMethodBinding.inflate(inflater, parent, false)
        val bindingCheckoutButton = CheckoutButtonBinding.inflate(inflater, parent, false)

        return when (viewType) {
            SHOW_ADDRESS -> OrderAddressViewHolder(bindingAddress, itemListener)
            SHOW_ORDER_LIST -> OrderListViewHolder(bindingOrderList, itemListener)
            SHOW_ORDER_TOTAL -> OrderTotalViewHolder(bindingOrderTotal, itemListener)
            SHOW_PAYMENT_METHOD -> PaymentMethodViewHolder(bindingPaymentMethod, itemListener)
            SHOW_CHECKOUT_BUTTON -> CheckoutButtonViewHolder(bindingCheckoutButton, itemListener)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        when (position) {
            SHOW_ADDRESS -> {
                (holder as OrderAddressViewHolder).bind(item as Address)
                return
            }

            SHOW_ORDER_TOTAL -> {
                (holder as OrderTotalViewHolder).bind(item as Pair<Int, Int>)
                return
            }

            SHOW_PAYMENT_METHOD -> {
                (holder as PaymentMethodViewHolder).bind()
                return
            }

            SHOW_CHECKOUT_BUTTON -> {
                (holder as CheckoutButtonViewHolder).bind()
                return
            }

            else -> {
                (holder as OrderListViewHolder).bind(item as Menu)
                return
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}