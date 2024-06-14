package com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.databinding.OrderTotalPriceBinding
import com.example.alfaresto_customersapp.ui.components.listener.ItemListener

class OrderTotalViewHolder (
    private val view: OrderTotalPriceBinding,
    private val onAddressClicked: ItemListener?
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