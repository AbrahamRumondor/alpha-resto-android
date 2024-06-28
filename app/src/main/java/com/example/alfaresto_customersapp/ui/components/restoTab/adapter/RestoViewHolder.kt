package com.example.alfaresto_customersapp.ui.components.restoTab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alfaresto_customersapp.databinding.MenuItemBinding
import com.example.alfaresto_customersapp.domain.model.Menu

class RestoViewHolder(
    private var binding: MenuItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(menu: Menu) {
        binding.let {
            it.tvMenuName.text = menu.name
            it.tvMenuPrice.text = menu.price.toString()
            Glide.with(it.root)
                .load(menu.image)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .into(it.ivMenuImage)
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
