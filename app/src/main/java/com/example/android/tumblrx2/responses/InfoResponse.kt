package com.example.android.tumblrx2.responses

data class InfoResponse(
    val blogs: List<String>,
    val default_post_format: String,
    val email: String,
    val following: Int,
    val id: String,
    val likes: Int,
    val name: String,
    val primary_blog: String,
    val settings: Settings
)