package com.example.alfaresto_customersapp.utils.singleton

import java.util.regex.Pattern

object Constants {
    const val SERVER_IP = "http://192.168.196.165:8081/"
    val passwordPatterns: Pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
    const val login = "login"
    const val isLoggedIn = "isLoggedIn"
}