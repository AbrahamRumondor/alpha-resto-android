package com.example.alfaresto_customersapp.ui.components.restoTab.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.ui.components.listener.MenuListener

class RestoAdapter : RecyclerView.Adapter<RestoViewHolder>() {

    private var menuList: List<Menu> = listOf()
    private var menuListener: MenuListener? = null
    private var itemClickListener: ((Menu) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestoViewHolder {
        return RestoViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: RestoViewHolder, position: Int) {
        val menuItem = menuList[position]
        holder.bind(menuItem, position, menuListener, itemClickListener)
    }

    fun submitMenuList(menuList: List<Menu>) {
        this.menuList = menuList
        notifyDataSetChanged()
    }

    fun setItemClickListener(listener: (Menu) -> Unit) {
        this.itemClickListener = listener
    }
    fun setItemListener(menuListener: MenuListener) {
        this.menuListener = menuListener
    }
}