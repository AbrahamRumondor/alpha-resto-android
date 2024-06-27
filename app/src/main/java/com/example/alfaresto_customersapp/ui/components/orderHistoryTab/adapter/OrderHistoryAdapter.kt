package com.example.alfaresto_customersapp.ui.components.orderHistoryTab.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.alfaresto_customersapp.domain.model.OrderHistory

class OrderHistoryAdapter(
    private val itemClickListener: (OrderHistory) -> Unit
) : ListAdapter<OrderHistory, OrderHistoryViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        return OrderHistoryViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val order = getItem(position)
        holder.bind(order)
        holder.itemView.setOnClickListener {
            itemClickListener(order)
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<OrderHistory>() {
            override fun areItemsTheSame(oldItem: OrderHistory, newItem: OrderHistory): Boolean {
                return oldItem.orderID == newItem.orderID
            }

            override fun areContentsTheSame(oldItem: OrderHistory, newItem: OrderHistory): Boolean {
                return oldItem == newItem
            }
        }
    }
}