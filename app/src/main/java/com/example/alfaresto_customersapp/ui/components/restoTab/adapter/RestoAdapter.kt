package com.example.alfaresto_customersapp.ui.components.restoTab.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.domain.model.Menu

class RestoAdapter : RecyclerView.Adapter<RestoViewHolder>() {

    private var menuList: List<Menu> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestoViewHolder {
        return RestoViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: RestoViewHolder, position: Int) {
        val menuItem = menuList[position]
        holder.bind(menuItem)
    }

    fun submitMenuList(menuList: List<Menu>) {
        this.menuList = menuList
        Log.d("MENU adapter", "Menu list submitted: $menuList")
    }

}
