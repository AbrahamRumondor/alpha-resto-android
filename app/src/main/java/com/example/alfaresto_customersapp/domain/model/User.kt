package com.example.alfaresto_customersapp.domain.model

import com.google.firebase.firestore.PropertyName

data class User(
    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var id: String = "",

    @get:PropertyName("user_name")
    @set:PropertyName("user_name")
    var name: String = "",

    @get:PropertyName("user_no_telp")
    @set:PropertyName("user_no_telp")
    var noPhone: String = "",

    @get:PropertyName("user_email")
    @set:PropertyName("user_email")
    var email: String = "",

    @get:PropertyName("user_password")
    @set:PropertyName("user_password")
    var password: String = "",

//    val address: List<Address> = mutableListOf(),
)
