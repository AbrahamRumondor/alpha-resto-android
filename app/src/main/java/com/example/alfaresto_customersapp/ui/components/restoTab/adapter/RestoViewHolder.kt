package com.example.alfaresto_customersapp.ui.components.restoTab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.MenuItemBinding
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.ui.components.listener.MenuListener

class RestoViewHolder(
    private var binding: MenuItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(menu: Menu, position: Int, listener: MenuListener?) {
        binding.run {
            it.tvMenuName.text = menu.menuName
            it.tvMenuPrice.text = menu.menuPrice.toString()
            Glide.with(it.root)
                .load(menu.menuImage)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .into(it.ivMenuImage)
            tvOrderQty.text = menu.orderCartQuantity.toString()

            val isVisible = menu.orderCartQuantity != 0
            clActionButtons.visibility = View.VISIBLE
            menuAddBtn.visibility = View.INVISIBLE
//            clActionButtons.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
//            menuAddBtn.visibility = if (isVisible) View.INVISIBLE else View.VISIBLE

            val clickListener = View.OnClickListener { view ->
                when (view) {
                    menuAddBtn, btnAddOrder -> listener?.onAddItemClicked(position, menuId = menu.id)
                    btnDecreaseOrder -> listener?.onDecreaseItemClicked(position, menuId = menu.id)
                }
            }

            menuAddBtn.setOnClickListener(clickListener)
            btnAddOrder.setOnClickListener(clickListener)
            btnDecreaseOrder.setOnClickListener(clickListener)
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
