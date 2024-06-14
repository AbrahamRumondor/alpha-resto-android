package com.example.alfaresto_customersapp.ui.components.restoTab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.MenuItemBinding
import com.example.alfaresto_customersapp.domain.model.Menu

class RestoViewHolder(
    private var binding: MenuItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(menu: Menu) {
        binding.menuNameTv.text = menu.menuName
        binding.menuPriceTv.text = menu.menuPrice.toString()
        binding.menuImageIv.setImageResource(R.drawable.dummy)
    }

    companion object {
        fun create(view: ViewGroup): RestoViewHolder {
            val inflater = LayoutInflater.from(view.context)
            val binding = MenuItemBinding.inflate(inflater, view, false)
            return RestoViewHolder(binding)
        }
    }
}
