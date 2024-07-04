package com.example.alfaresto_customersapp.ui.components.orderHistoryDetailPage.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.domain.model.OrderItem

class OrderHistoryDetailItemsAdapter : RecyclerView.Adapter<OrderHistoryDetailItemsViewHolder>() {

    private var orderHistoryDetailItemsList: List<OrderItem> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderHistoryDetailItemsViewHolder {
        return OrderHistoryDetailItemsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: OrderHistoryDetailItemsViewHolder, position: Int) {
        val orderHistoryDetailItem = orderHistoryDetailItemsList[position]
        holder.bind(orderHistoryDetailItem)
    }

    override fun getItemCount(): Int {
        return orderHistoryDetailItemsList.size
    }

    fun submitOrderHistoryDetailItemsList(orderHistoryDetailItemsList: List<OrderItem>) {
        this.orderHistoryDetailItemsList = orderHistoryDetailItemsList
        notifyItemRangeChanged(0, orderHistoryDetailItemsList.size)
    }
}