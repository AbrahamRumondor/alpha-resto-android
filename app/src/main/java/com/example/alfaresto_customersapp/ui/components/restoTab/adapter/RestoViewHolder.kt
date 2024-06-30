package com.example.alfaresto_customersapp.ui.components.restoTab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alfaresto_customersapp.databinding.MenuItemBinding
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.ui.components.listener.MenuListener

class RestoViewHolder(
    private var binding: MenuItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(menu: Menu, position: Int, listener: MenuListener?, itemClickListener: ((Menu) -> Unit)? = null) {
        binding.let {
            it.tvMenuName.text = menu.name
            it.tvMenuPrice.text = menu.price.toString()
            Glide.with(it.root)
                .load(menu.image)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .into(it.ivMenuImage)

            val isVisible = menu.orderCartQuantity != 0
            it.btnMenuAdd.visibility = View.VISIBLE
            it.btnAddOrder.visibility = View.INVISIBLE
            it.btnMenuAdd.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
            it.btnDecreaseOrder.visibility = if (isVisible) View.INVISIBLE else View.VISIBLE

            val clickListener = View.OnClickListener { view ->
                when (view) {
                    it.btnMenuAdd, it.btnAddOrder -> listener?.onAddItemClicked(position, menuId = menu.id)
                    it.btnDecreaseOrder -> listener?.onDecreaseItemClicked(position, menuId = menu.id)
                    else -> itemClickListener?.invoke(menu)
                }
            }

            it.root.setOnClickListener(clickListener)
            it.btnMenuAdd.setOnClickListener(clickListener)
            it.btnAddOrder.setOnClickListener(clickListener)
            it.btnDecreaseOrder.setOnClickListener(clickListener)
        }
    }

    companion object {
        fun create(view: ViewGroup): RestoViewHolder {
            val inflater = LayoutInflater.from(view.context)
            val binding = MenuItemBinding.inflate(inflater, view, false)
            return RestoViewHolder(binding)
        }
    }
}
