package com.example.alfaresto_customersapp.ui.components.orderHistoryTab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.OrderHistoryItemBinding
import com.example.alfaresto_customersapp.domain.model.OrderHistory
import com.example.alfaresto_customersapp.domain.model.OrderStatus

class OrderHistoryViewHolder(
    private var binding: OrderHistoryItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(order: OrderHistory) {
        binding.let {
            it.tvOrderDate.text = order.orderDate
            it.tvOrderPrice.text = order.orderTotalPrice.toString()
            order.orderStatus.name.let { status ->
                it.tvOrderStatus.text = status
                it.cvOrderStatus.setCardBackgroundColor(
                    it.root.context.getColor(
                        when (status) {
                            OrderStatus.DELIVERED.name -> R.color.green
                            OrderStatus.ON_DELIVERY.name -> R.color.orange
                            else -> R.color.yellow
                        }
                    )
                )
            }
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