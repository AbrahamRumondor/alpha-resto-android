package com.example.alfaresto_customersapp.ui.components.orderHistoryTab.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.OrderHistoryItemBinding
import com.example.alfaresto_customersapp.domain.model.OrderHistory
import com.example.alfaresto_customersapp.domain.model.OrderStatus
import com.example.alfaresto_customersapp.ui.components.listener.OrderHistoryListener

class OrderHistoryViewHolder(
    private var binding: OrderHistoryItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(order: OrderHistory, listener: OrderHistoryListener?) {
        binding.run {
            tvOrderDate.text = order.orderDate
            tvOrderPrice.text = order.orderTotalPrice.toString()
            order.orderStatus.name.let { status ->
                tvOrderStatus.text = status
                cvOrderStatus.setCardBackgroundColor(
                    root.context.getColor(
                        when (status) {
                            OrderStatus.DELIVERED.name -> R.color.green
                            OrderStatus.ON_DELIVERY.name -> R.color.orange
                            else -> R.color.yellow
                        }
                    )
                )
            }
            tvOrderAddress.text = order.addressLabel

            clOrderHistoryItem.setOnClickListener {
                Log.d("OrderHistoryViewHolder", "Order ID: ${order.id}")
                listener?.onOrderClicked(order)
            }

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