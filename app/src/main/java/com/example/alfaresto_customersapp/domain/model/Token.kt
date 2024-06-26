package com.example.alfaresto_customersapp.domain.model

import com.google.firebase.firestore.PropertyName

data class Token (
    @get:PropertyName("user_token")
    @set:PropertyName("user_token")
    var userToken: String = ""
)