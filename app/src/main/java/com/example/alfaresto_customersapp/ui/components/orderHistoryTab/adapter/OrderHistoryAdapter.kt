package com.example.alfaresto_customersapp.ui.components.orderHistoryTab.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.alfaresto_customersapp.domain.model.OrderHistory
import com.example.alfaresto_customersapp.ui.components.listener.OrderHistoryListener

class OrderHistoryAdapter(
    private val onItemClick: (OrderHistory) -> Unit
) : ListAdapter<OrderHistory, OrderHistoryViewHolder>(diffUtil) {

    private var orderHistoryListener: OrderHistoryListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        return OrderHistoryViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val order = getItem(position)
        holder.bind(order, orderHistoryListener)
        holder.itemView.setOnClickListener {
            onItemClick(order)
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<OrderHistory>() {
            override fun areItemsTheSame(oldItem: OrderHistory, newItem: OrderHistory): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: OrderHistory, newItem: OrderHistory): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun setItemListener(orderHistoryListener: OrderHistoryListener) {
        this.orderHistoryListener = orderHistoryListener
    }
}