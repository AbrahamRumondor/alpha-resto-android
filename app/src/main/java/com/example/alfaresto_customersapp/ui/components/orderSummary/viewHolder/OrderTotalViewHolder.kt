package com.example.alfaresto_customersapp.ui.components.orderSummary.viewHolder

import android.view.View.VISIBLE
import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.databinding.OrderSummaryTotalPriceBinding
import java.text.NumberFormat
import java.util.Locale

class OrderTotalViewHolder (
    private val view: OrderSummaryTotalPriceBinding,
) : RecyclerView.ViewHolder(view.root) {

    fun bind(total: Pair<Int, Int>?) {
        view.run {
            if (total != null && total.first >= 1) {
                tvTotalOrderQty.text = total.first.toString()
                tvTotalPrice.text = formattedPrice(total.second)
                tvTotalItems.text = if (total.first == 1) " item" else " items"
            }
        }
    }


    private fun formattedPrice(price: Int): String {
        return "Rp ${NumberFormat.getNumberInstance(Locale("id", "ID")).format(price)}"
    }
}