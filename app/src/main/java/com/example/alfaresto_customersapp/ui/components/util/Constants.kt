package com.example.alfaresto_customersapp.ui.components.util

import java.util.regex.Pattern

object Constants {
    val passwordPatterns: Pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
}