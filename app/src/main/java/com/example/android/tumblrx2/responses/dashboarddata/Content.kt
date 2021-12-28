package com.example.android.tumblrx2.responses.dashboarddata

data class Content(
    val formatting: List<Formatting>,
    val media: String,
    val provider: String,
    val subtype: String,
    val text: String,
    val type: String,
    val url: String
)