package com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.databinding.CheckoutButtonBinding
import com.example.alfaresto_customersapp.ui.components.listener.OrderSummaryItemListener

class CheckoutButtonViewHolder (
    private val view: CheckoutButtonBinding,
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