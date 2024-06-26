package com.example.alfaresto_customersapp.utils

import com.example.alfaresto_customersapp.domain.error.DataError
import com.example.alfaresto_customersapp.domain.error.Result

fun DataError.getText(): String {
    return when (this) {
        DataError.Network.REQUEST_TIMEOUT -> REQUEST_TIMEOUT

        DataError.Network.TOO_MANY_REQUESTS -> TOO_MANY_REQUESTS

        DataError.Network.NO_INTERNET -> NO_INTERNET

        DataError.Network.PAYLOAD_TOO_LARGE -> PAYLOAD_TOO_LARGE

        DataError.Network.SERVER_ERROR -> SERVER_ERROR

        DataError.Network.SERIALIZATION -> SERIALIZATION

        DataError.Network.UNKNOWN -> UNKNOWN

        DataError.Local.DATABASE_ISSUE -> DATABASE_ISSUE
    }
}

fun Result.Error<*, DataError>.getErrorText(): String {
    return error.getText()
}

const val REQUEST_TIMEOUT = "The request timed out"
const val TOO_MANY_REQUESTS = "You\'ve hit your rate limit"
const val NO_INTERNET = "Couldn\'t reach server. Check your internet connection."
const val PAYLOAD_TOO_LARGE = "The file you tried to upload was too large."
const val SERVER_ERROR = "Oops, something went wrong. Please try again."
const val SERIALIZATION = "Oops, couldn\'t parse data."
const val UNKNOWN = "An unknown error occurred"
const val DATABASE_ISSUE = "Error happened while working with Database"


