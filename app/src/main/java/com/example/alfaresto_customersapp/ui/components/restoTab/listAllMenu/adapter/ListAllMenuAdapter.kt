package com.example.alfaresto_customersapp.ui.components.restoTab.listAllMenu.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.ui.components.listener.MenuListener

class ListAllMenuAdapter : PagingDataAdapter<Menu, ListAllMenuViewHolder>(MenuComparator) {
    private var menuListener: MenuListener? = null
    private var itemClickListener: ((Menu) -> Unit)? = null

    override fun onBindViewHolder(holder: ListAllMenuViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, position, menuListener, itemClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAllMenuViewHolder {
        return ListAllMenuViewHolder.create(parent)
    }

    fun setItemClickListener(listener: (Menu) -> Unit) {
        itemClickListener = listener
    }

    companion object {
        val MenuComparator = object : DiffUtil.ItemCallback<Menu>() {
            override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
                return oldItem == newItem
            }
        }
    }
}