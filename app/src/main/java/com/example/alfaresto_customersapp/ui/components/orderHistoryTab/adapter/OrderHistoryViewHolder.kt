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
        binding.tvOrderDate.text = order.orderDate
        binding.tvOrderPrice.text = order.orderTotalPrice.toString()

        binding.cvOrderStatus.setCardBackgroundColor(
            when (order.orderStatus) {
                "delivered" -> binding.root.context.getColor(R.color.green)
                "process" -> binding.root.context.getColor(R.color.yellow)
                else -> binding.root.context.getColor(R.color.orange)
            }
        )

        binding.tvOrderStatus.text = order.orderStatus
        binding.tvOrderAddress.text = order.addressLabel
    }

    companion object {
        fun create(view: ViewGroup): OrderHistoryViewHolder {
            val inflater = LayoutInflater.from(view.context)
            val binding = OrderHistoryItemBinding.inflate(inflater, view, false)
            return OrderHistoryViewHolder(binding)
        }
    }
}