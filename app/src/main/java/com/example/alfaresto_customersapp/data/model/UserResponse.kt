package com.example.alfaresto_customersapp.data.model

import com.example.alfaresto_customersapp.domain.model.User
import com.google.firebase.firestore.PropertyName

data class UserResponse(
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
) {
    companion object {
        fun transform(itemResponse: UserResponse): User {
            return User(
                id = itemResponse.id,
                name = itemResponse.name,
                noPhone = itemResponse.noPhone,
                email = itemResponse.email,
                password = itemResponse.password,
            )
        }
    }
}
