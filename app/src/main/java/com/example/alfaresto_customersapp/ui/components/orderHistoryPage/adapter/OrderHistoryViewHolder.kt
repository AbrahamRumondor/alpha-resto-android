package com.example.alfaresto_customersapp.ui.components.orderHistoryPage.adapter

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
            tvOrderDate.text = order.formattedDate()
            tvOrderPrice.text = order.formattedPrice()
            tvOrderAddress.text = order.addressLabel
            order.orderStatus.name.let { status ->
                tvOrderStatus.text = status
                root.context.getString(
                    when (status) {
                        OrderStatus.DELIVERED.name -> R.string.delivered_status
                        OrderStatus.ON_DELIVERY.name -> R.string.on_delivery_status
                        OrderStatus.ON_PROCESS.name -> R.string.on_process_status
                        else -> R.string.canceled
                    }
                ).let { tvOrderStatus.text = it }
                cvOrderStatus.setCardBackgroundColor(
                    root.context.getColor(
                        when (status) {
                            OrderStatus.DELIVERED.name -> R.color.green
                            OrderStatus.ON_DELIVERY.name -> R.color.button_light_orange
                            OrderStatus.ON_PROCESS.name -> R.color.yellow
                            else -> R.color.red
                        }
                    )
                )
                when (status) {
                    OrderStatus.CANCELED.name -> {
                        tvOrderStatus.setTextColor(root.context.getColor(R.color.white))
                    }
                    else -> {
                        tvOrderStatus.setTextColor(root.context.getColor(R.color.black))
                    }
                }
            }

            clOrderHistoryItem.setOnClickListener {
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