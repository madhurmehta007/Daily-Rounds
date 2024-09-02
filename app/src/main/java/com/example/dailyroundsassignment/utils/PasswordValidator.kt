package com.example.dailyroundsassignment.utils

import java.util.regex.Pattern

class PasswordValidator {

    companion object {
        fun validatePassword(password: String): String {
            val minLength = 8
            val pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$")

            return when {
                password.length >= minLength && pattern.matcher(password).matches() -> "Strong"
                password.length >= minLength -> "Moderate"
                else -> "Weak"
            }
        }
    }
}