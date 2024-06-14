package com.example.alfaresto_customersapp.ui.components.restoTab.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.domain.model.Menu

class RestoAdapter : RecyclerView.Adapter<RestoViewHolder>() {

    private var restoList: List<Menu> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestoViewHolder {
        return RestoViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return restoList.size
    }

    override fun onBindViewHolder(holder: RestoViewHolder, position: Int) {
        val restoItem = restoList[position]
        holder.bind(restoItem)
    }

    fun submitRestoList(restoList: List<Menu>) {
        this.restoList = restoList
    }

}
