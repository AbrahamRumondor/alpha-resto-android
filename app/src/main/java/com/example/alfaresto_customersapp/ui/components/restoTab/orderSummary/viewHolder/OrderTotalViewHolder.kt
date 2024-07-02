package com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.databinding.OrderSummaryTotalPriceBinding
import com.example.alfaresto_customersapp.ui.components.listener.OrderSummaryItemListener

class OrderTotalViewHolder (
    private val view: OrderSummaryTotalPriceBinding,
) : RecyclerView.ViewHolder(view.root) {

    fun bind(total: Pair<Int, Int>?) {
        view.run {
            if (total != null) {
                tvTotalOrderQty.text = total.first.toString()
                tvTotalPrice.text = total.second.toString()
            }
        }
    }
}