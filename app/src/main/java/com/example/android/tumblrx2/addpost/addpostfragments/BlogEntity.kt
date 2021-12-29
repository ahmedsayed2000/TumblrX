package com.example.android.tumblrx2.addpost.addpostfragments

data class BlogEntity(
    val _id: String,
    val askAnon: Boolean,
    val description: String,
    val handle: String,
    val headerImage: String,
    val isAvatarCircle: Boolean,
    val isPrimary: Boolean,
    val isPrivate: Boolean,
    val title: String
)