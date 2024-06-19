package com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.databinding.OrderItemBinding
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.ui.components.listener.OrderSummaryItemListener

class OrderListViewHolder (
    private val view: OrderItemBinding,
    private val itemListener: OrderSummaryItemListener?,
) : RecyclerView.ViewHolder(view.root) {

    fun bind(menu: Menu?, position: Int) {
        view.run {
            if (menu != null) {
                tvFoodTitle.text = menu.menuName
                tvFoodDesc.text = menu.menuDesc
                tvFoodPrice.text = menu.menuPrice.toString()
                tvFoodQty.text = menu.orderCartQuantity.toString()
            }
            ivOrderAdd.setOnClickListener {
                itemListener?.onAddItemClicked(position)
            }
            ivOrderDecrease.setOnClickListener {
                itemListener?.onDecreaseItemClicked(position)
            }
            ivOrderDelete.setOnClickListener {
                itemListener?.onDeleteItemClicked(position)
            }
        }
    }
}