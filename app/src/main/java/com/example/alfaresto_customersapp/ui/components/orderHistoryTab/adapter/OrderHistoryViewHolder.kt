package com.example.alfaresto_customersapp.ui.components.orderHistoryTab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.OrderHistoryItemBinding
import com.example.alfaresto_customersapp.domain.model.OrderHistory

class OrderHistoryViewHolder(
    private var binding: OrderHistoryItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(order: OrderHistory) {
        binding.let {
            it.tvOrderDate.text = order.orderDate
            it.tvOrderPrice.text = order.orderTotalPrice.toString()
            it.cvOrderStatus.setCardBackgroundColor(
                when (order.orderStatus) {
                    "delivered" -> it.root.context.getColor(R.color.green)
                    "process" -> it.root.context.getColor(R.color.yellow)
                    else -> it.root.context.getColor(R.color.orange)
                }
            )
            it.tvOrderStatus.text = order.orderStatus
            it.tvOrderAddress.text = order.addressLabel
        }
    }

    companion object {
        fun create(view: ViewGroup): OrderHistoryViewHolder {
            val inflater = LayoutInflater.from(view.context)
            val binding = OrderHistoryItemBinding.inflate(inflater, view, false)
            return OrderHistoryViewHolder(binding)
        }
    }
}