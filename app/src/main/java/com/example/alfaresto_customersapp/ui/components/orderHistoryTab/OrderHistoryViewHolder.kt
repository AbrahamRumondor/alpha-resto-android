package com.example.alfaresto_customersapp.ui.components.orderHistoryTab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.OrderHistoryItemBinding
import com.example.alfaresto_customersapp.domain.model.Order

class OrderHistoryViewHolder(
    private var binding: OrderHistoryItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(order: Order) {
        binding.orderDateTv.text = order.orderDate
        binding.orderStatusTv.text = order.statusDelivery
        binding.orderPriceTv.text = order.orderTotalPrice.toString()

        binding.orderStatusCv.setCardBackgroundColor(
            when (order.statusDelivery) {
                "Delivered" -> binding.root.context.getColor(R.color.green)
                "On Process" -> binding.root.context.getColor(R.color.yellow)
                else -> binding.root.context.getColor(R.color.black)
            }
        )
    }

    companion object {
        fun create(view: ViewGroup): OrderHistoryViewHolder {
            val inflater = LayoutInflater.from(view.context)
            val binding = OrderHistoryItemBinding.inflate(inflater, view, false)
            return OrderHistoryViewHolder(binding)
        }
    }
}
