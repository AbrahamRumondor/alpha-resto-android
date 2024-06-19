package com.example.alfaresto_customersapp.ui.components.restoTab.listAllMenu.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.alfaresto_customersapp.domain.model.Menu

class ListAllMenuAdapter : PagingDataAdapter<Menu, ListAllMenuViewHolder>(MenuComparator) {
    override fun onBindViewHolder(holder: ListAllMenuViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAllMenuViewHolder {
        return ListAllMenuViewHolder.create(parent)
    }

    companion object {
        val MenuComparator = object : DiffUtil.ItemCallback<Menu>() {
            override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
                return oldItem.menuId == newItem.menuId
            }

            override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
                return oldItem == newItem
            }
        }
    }
}