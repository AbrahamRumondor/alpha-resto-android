package com.example.alfaresto_customersapp.ui.components.restoTab.listAllMenu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alfaresto_customersapp.databinding.AllMenuItemBinding
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.ui.components.listener.MenuListener

class ListAllMenuViewHolder(
    private var binding: AllMenuItemBinding
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
            it.btnPlus.visibility = View.INVISIBLE
            it.btnMenuAdd.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
            it.btnMinus.visibility = if (isVisible) View.INVISIBLE else View.VISIBLE

            val clickListener = View.OnClickListener { view ->
                when (view) {
                    it.btnMenuAdd, it.btnPlus -> listener?.onAddItemClicked(
                        position,
                        menuId = menu.id
                    )

                    it.btnMinus -> listener?.onDecreaseItemClicked(position, menuId = menu.id)

                    else -> {
                        itemClickListener?.invoke(menu)
                    }
                }
            }

            it.root.setOnClickListener(clickListener)
            it.btnMenuAdd.setOnClickListener(clickListener)
            it.btnPlus.setOnClickListener(clickListener)
            it.btnMinus.setOnClickListener(clickListener)
        }
    }

    companion object {
        fun create(view: ViewGroup): ListAllMenuViewHolder {
            val inflater = LayoutInflater.from(view.context)
            val binding = AllMenuItemBinding.inflate(inflater, view, false)
            return ListAllMenuViewHolder(binding)
        }
    }
}
