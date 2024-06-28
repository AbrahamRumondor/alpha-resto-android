package com.example.alfaresto_customersapp.ui.components.orderHistoryDetailPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.ItemOrderDetailBinding
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.model.OrderItem

class OrderItemsAdapter(private val items: List<Pair<OrderItem, Menu>>) : RecyclerView.Adapter<OrderItemsAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemOrderDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(orderItem: OrderItem, menu: Menu) {
            with(binding) {
                tvMenuName.text = menu.name
                tvMenuDesc.text = menu.description
                tvMenuPrice.text = "Rp ${String.format("%.2f", orderItem.menuPrice)}"
                tvMenuQty.text = "Qty: ${orderItem.quantity}"
                Glide.with(itemView.context)
                    .load(menu.image)
                    .error(com.google.android.material.R.drawable.mtrl_ic_error) // Error image if loading fails
                    .into(ivMenuImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (orderItem, menu) = items[position]
        holder.bind(orderItem, menu)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}