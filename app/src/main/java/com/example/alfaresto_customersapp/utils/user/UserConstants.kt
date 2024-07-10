package com.example.alfaresto_customersapp.utils.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.Shipment

object UserConstants {
    var USER_TOKEN: String? = null
    var USER_ADDRESS : Address? = null
    var USER_ID: String? = null
    var SHIPMENT: MutableLiveData<Shipment> = MutableLiveData()
    var ORDER_CHECKOUT_STATUS = false
}