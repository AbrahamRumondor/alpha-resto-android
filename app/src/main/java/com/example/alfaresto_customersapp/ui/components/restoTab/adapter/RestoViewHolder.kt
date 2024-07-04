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

    fun bind(menu: Menu, position: Int, listener: MenuListener?, itemClickListener: ((Menu) -> Unit)?) {
        binding.run {
            tvMenuName.text = menu.name
            tvMenuPrice.text = menu.formattedPrice()
            tvOrderQty.text = menu.orderCartQuantity.toString()

            Glide.with(root)
                .load(menu.image)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .into(ivMenuImage)

            val isVisible = menu.orderCartQuantity != 0
            clActionButtons.visibility = View.VISIBLE
            btnMenuAdd.visibility = View.INVISIBLE
            clActionButtons.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
            btnMenuAdd.visibility = if (isVisible) View.INVISIBLE else View.VISIBLE

            val clickListener = View.OnClickListener { view ->
                when (view) {
                    btnMenuAdd, btnAddOrder -> listener?.onAddItemClicked(
                        position,
                        menuId = menu.id
                    )

                    btnDecreaseOrder -> listener?.onDecreaseItemClicked(position, menuId = menu.id)

                    ivMenuImage -> {
                        itemClickListener?.invoke(menu)
                    }
                }
            }

            root.setOnClickListener(clickListener)
            btnMenuAdd.setOnClickListener(clickListener)
            btnAddOrder.setOnClickListener(clickListener)
            btnDecreaseOrder.setOnClickListener(clickListener)
            ivMenuImage.setOnClickListener(clickListener)
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