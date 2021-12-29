package com.example.android.tumblrx2.responses.likes

data class BlogAttribution(
    val _id: String,
    val avatar: String,
    val handle: String,
    val isAvatarCircle: Boolean,
    val owner: String,
    val title: String
)