package com.example.alfaresto_customersapp.ui.components.restoTab.listAllMenu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.AllMenuItemBinding
import com.example.alfaresto_customersapp.domain.model.Menu

class ListAllMenuViewHolder(
    private var binding: AllMenuItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(menu: Menu) {
        binding.menuNameTv.text = menu.menuName
        binding.menuPriceTv.text = menu.menuPrice.toString()
        binding.menuImageIv.setImageResource(R.drawable.dummy)
    }

    companion object {
        fun create(view: ViewGroup): ListAllMenuViewHolder {
            val inflater = LayoutInflater.from(view.context)
            val binding = AllMenuItemBinding.inflate(inflater, view, false)
            return ListAllMenuViewHolder(binding)
        }
    }
}
