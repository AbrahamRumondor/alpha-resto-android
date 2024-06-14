package com.example.alfaresto_customersapp.ui.components.listener

import com.example.alfaresto_customersapp.domain.model.Address

interface ItemListener {
    fun onAddressClicked(address: Address){}
    // uses {} for default implementation (so dont have to impelemnts).
}