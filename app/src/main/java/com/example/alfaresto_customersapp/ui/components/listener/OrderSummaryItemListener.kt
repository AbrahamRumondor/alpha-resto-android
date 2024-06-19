package com.example.alfaresto_customersapp.ui.components.listener

import android.view.View
import com.example.alfaresto_customersapp.databinding.OrderPaymentMethodBinding
import com.example.alfaresto_customersapp.domain.model.Address

interface OrderSummaryItemListener {
    fun onAddressClicked(position: Int)
    fun onAddItemClicked(position: Int)
    fun onDecreaseItemClicked(position: Int)
    fun onDeleteItemClicked(position: Int)
    fun onRadioButtonClicked(position: Int, id: Int)
    fun onPaymentMethodClicked(view: OrderPaymentMethodBinding)
    fun onCheckoutButtonClicked()
    // uses {} for default implementation (so dont have to impelemnts).
}