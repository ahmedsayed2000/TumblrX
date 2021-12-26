package com.example.android.tumblrx2.blogapis

data class CreateBlogBody(
    val handle: String,
    val private: Boolean,
    val title: String
)