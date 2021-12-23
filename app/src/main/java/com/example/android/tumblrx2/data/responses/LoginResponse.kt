package com.example.android.tumblrx2.data.responses

import com.example.android.tumblrx2.login.User

data class LoginResponse(
    val status: String,
    val token: String,
    val user: User
)
