package com.example.android.tumblrx2.responses.chatsresponse

data class ChatObject(
    val avatar: String,
    val blogHandle: String,
    val chatId: String,
    val isMe: Boolean,
    val message: String,
    val messageDate: String,
    val messageSenderId: String,
    val primaryBlogId: String,
    val textedUser: String
)