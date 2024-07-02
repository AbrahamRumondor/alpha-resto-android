package com.example.alfaresto_customersapp.ui.components.listener

import android.view.View
import com.example.alfaresto_customersapp.databinding.OrderSummaryPaymentMethodBinding
import com.example.alfaresto_customersapp.domain.model.Address

interface OrderSummaryItemListener {
    fun onAddressClicked(position: Int)
    fun onAddItemClicked(position: Int,  menuId: String)
    fun onDecreaseItemClicked(position: Int,  menuId: String)
    fun onDeleteItemClicked(position: Int, menuId: String)
    fun onRadioButtonClicked(position: Int, id: Int)
    fun onPaymentMethodClicked(view: OrderSummaryPaymentMethodBinding)
    fun onCheckoutButtonClicked()
    // uses {} for default implementation (so dont have to impelemnts).
}