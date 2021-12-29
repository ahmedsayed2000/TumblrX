package com.example.android.tumblrx2.responses.postsdata

data class Content(
    val formatting: List<Formatting>,
    val subtype: String,
    val text: String,
    val type: String
)