package com.example.alfaresto_customersapp.ui.components.orderHistoryDetailPage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alfaresto_customersapp.databinding.ItemOrderDetailBinding
import com.example.alfaresto_customersapp.domain.model.OrderItem
import timber.log.Timber

class OrderHistoryDetailItemsViewHolder(
    private var binding: ItemOrderDetailBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(orderItem: OrderItem) {
        binding.run {
            tvMenuName.text = orderItem.menuName
            tvMenuPrice.text = String.format("Rp %,d", orderItem.menuPrice)
            tvMenuQty.text = "${orderItem.quantity} pcs"
            Timber.d("OrderItem: ${orderItem.menuImage}")
            Glide.with(root)
                .load(orderItem.menuImage)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .into(ivMenuImage)
        }
    }

    companion object {
        fun create(view: ViewGroup): OrderHistoryDetailItemsViewHolder {
            val inflater = LayoutInflater.from(view.context)
            val binding = ItemOrderDetailBinding.inflate(inflater, view, false)
            return OrderHistoryDetailItemsViewHolder(binding)
        }
    }
}