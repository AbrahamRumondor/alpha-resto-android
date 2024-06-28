package com.example.alfaresto_customersapp.data.model

import com.example.alfaresto_customersapp.domain.model.Token
import com.google.firebase.firestore.PropertyName

data class TokenResponse(
    @get:PropertyName("user_token")
    @set:PropertyName("user_token")
    var userToken: String = ""
) {
    constructor() : this("")

    companion object {
        fun transform(tokenResponse: TokenResponse): Token {
            return Token(
                userToken = tokenResponse.userToken
            )
        }
    }
}
