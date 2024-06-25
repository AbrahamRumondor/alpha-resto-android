package com.example.alfaresto_customersapp.data.model

import com.example.alfaresto_customersapp.domain.model.User
import com.google.firebase.firestore.PropertyName

data class UserResponse(
    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userID: String = "",

    @get:PropertyName("user_name")
    @set:PropertyName("user_name")
    var userName: String = "",

    @get:PropertyName("user_no_telp")
    @set:PropertyName("user_no_telp")
    var userNoTelp: String = "",

    @get:PropertyName("user_email")
    @set:PropertyName("user_email")
    var userEmail: String = "",

    @get:PropertyName("user_address")
    @set:PropertyName("user_address")
    var userAddress: List<AddressResponse> = mutableListOf()
) {
    // Public no-argument constructor required by Firestore
    constructor() : this("", "", "", "", mutableListOf())

    companion object {
        fun transform(userResponse: UserResponse): User {
            return User(
                userID = userResponse.userID,
                userName = userResponse.userName,
                userNoTelp = userResponse.userNoTelp,
                userEmail = userResponse.userEmail,
                userAddress = userResponse.userAddress.map { AddressResponse.transform(it) }
            )
        }
    }
}