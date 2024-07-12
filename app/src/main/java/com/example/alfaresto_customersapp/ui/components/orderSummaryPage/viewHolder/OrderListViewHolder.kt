package com.example.alfaresto_customersapp.ui.components.orderSummaryPage.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alfaresto_customersapp.databinding.OrderSummaryItemBinding
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.ui.components.listener.OrderSummaryItemListener

class OrderListViewHolder(
    private val view: OrderSummaryItemBinding,
    private val itemListener: OrderSummaryItemListener?,
) : RecyclerView.ViewHolder(view.root) {

    fun bind(menu: Menu?, position: Int) {
        view.run {
            if (menu != null) {
                tvFoodTitle.text = menu.name
                tvFoodDesc.text = menu.description
                tvFoodPrice.text = menu.formattedPrice()
                tvFoodQty.text = menu.orderCartQuantity.toString()

                Glide.with(root)
                    .load(menu.image)
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .into(imgMenu)

                ivOrderAdd.setOnClickListener {
                    itemListener?.onAddItemClicked(position, menu.id)
                }
                ivOrderDecrease.setOnClickListener {
                    itemListener?.onDecreaseItemClicked(position, menu.id)
                }
            }
        }
    }
}