package com.example.android.tumblrx2.responses.messages

data class Message(
    val createdAt: String,
    val senderId: String,
    val text: String,
    val updatedAt: String
)