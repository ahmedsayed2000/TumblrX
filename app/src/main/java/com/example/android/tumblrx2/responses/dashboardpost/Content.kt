package com.example.android.tumblrx2.responses.dashboardpost

data class Content(
    val formatting: List<Any>,
    val media: String,
    val text: String,
    val type: String,
    val url: String
)