package com.example.android.tumblrx2.responses.following

data class FollowingBlog(
    val _id: String,
    val avatar: String,
    val friends: Boolean,
    val handle: String,
    val title: String
)