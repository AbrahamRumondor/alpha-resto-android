package com.example.alfaresto_customersapp.ui.components.orderHistoryTab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.databinding.OrderHistoryItemBinding
import com.example.alfaresto_customersapp.domain.model.Order

class OrderHistoryViewHolder(
    private var binding: OrderHistoryItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(order: Order) {
        binding.orderDateTv.text = order.orderDate
        binding.orderPriceTv.text = order.totalPrice.toString()
    }

    companion object {
        fun create(view: ViewGroup): OrderHistoryViewHolder {
            val inflater = LayoutInflater.from(view.context)
            val binding = OrderHistoryItemBinding.inflate(inflater, view, false)
            return OrderHistoryViewHolder(binding)
        }
    }
}
