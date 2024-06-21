package com.example.alfaresto_customersapp.ui.components.restoTab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.MenuItemBinding
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.ui.components.listener.MenuListener

class RestoViewHolder(
    private var binding: MenuItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(menu: Menu, position: Int, listener: MenuListener?) {
        binding.run {
            menuNameTv.text = menu.menuName
            menuPriceTv.text = menu.menuPrice.toString()
            menuImageIv.setImageResource(R.drawable.dummy)
            tvOrderQty.text = menu.orderCartQuantity.toString()

            val isVisible = menu.orderCartQuantity != 0
            clActionButtons.visibility = View.VISIBLE
            menuAddBtn.visibility = View.INVISIBLE
//            clActionButtons.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
//            menuAddBtn.visibility = if (isVisible) View.INVISIBLE else View.VISIBLE

            val clickListener = View.OnClickListener { view ->
                when (view) {
                    menuAddBtn, btnAddOrder -> listener?.onAddItemClicked(position, menuId = menu.menuId)
                    btnDecreaseOrder -> listener?.onDecreaseItemClicked(position, menuId = menu.menuId)
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
