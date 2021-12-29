package com.example.android.tumblrx2.responses

data class Data(
    val __v: Int,
    val _id: String,
    val ask: List<Any>,
    val avatar: String,
    val blockedTumblrs: List<Any>,
    val createdAt: String,
    val customApperance: CustomApperance,
    val followedBy: List<Any>,
    val handle: String,
    val headerImage: String,
    val isAvatarCircle: Boolean,
    val isPrimary: Boolean,
    val isPrivate: Boolean,
    val owner: String,
    val password: String,
    val posts: List<Any>,
    val settings: Settings,
    val submissions: List<Any>,
    val title: String,
    val updatedAt: String
)