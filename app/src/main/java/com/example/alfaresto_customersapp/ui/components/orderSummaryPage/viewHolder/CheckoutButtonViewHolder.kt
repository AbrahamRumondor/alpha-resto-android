package com.example.alfaresto_customersapp.ui.components.orderSummaryPage.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.databinding.OrderSummaryCheckoutButtonBinding
import com.example.alfaresto_customersapp.ui.components.listener.OrderSummaryItemListener

class CheckoutButtonViewHolder (
    private val view: OrderSummaryCheckoutButtonBinding,
    private val orderSummaryItemListener: OrderSummaryItemListener?
) : RecyclerView.ViewHolder(view.root) {

    fun bind() {
        view.run {
            btnCheckoutOrder.setOnClickListener {
                orderSummaryItemListener?.onCheckoutButtonClicked()
            }
        }
    }
}