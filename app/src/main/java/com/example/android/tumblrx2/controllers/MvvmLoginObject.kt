package com.example.android.tumblrx2.controllers

import org.junit.runner.RunWith
import org.junit.runners.JUnit4

object MvvmLoginObject {

    private val users =
        listOf<Pair<String, String>>(Pair("Gamal", "123456"), Pair("Ahmed", "456789"))

    fun loginValidateInput(email: String, password: String): Int {
        return if (email.isEmpty() || password.isEmpty()) -1
        else if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) 1
        else if (password.length < 6) 2
        else 0
    }

    fun loginChooseErrMsg(errCode: Int): String {
        return when (errCode) {
            -1 -> "Make sure all fields are filled"
            1 -> "Enter a valid email"
            else -> "Password is too short"
        }
    }

}