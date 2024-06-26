package com.example.alfaresto_customersapp.ui.components.listener

interface MenuListener {
    fun onAddItemClicked(position: Int, menuId: String)
    fun onDecreaseItemClicked(position: Int, menuId: String)
}