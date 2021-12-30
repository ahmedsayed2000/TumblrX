package com.example.android.tumblrx2.responses.dashboarddata

data class Trail(
    val __v: Int,
    val _id: String,
    val blogAttribution: BlogAttributionX,
    val commentsCount: Int,
    val content: List<ContentX>,
    val id: String,
    val likesCount: Int,
    val notes: String,
    val notesCount: Int,
    val postType: String,
    val publishedOn: Int,
    val reblogsCount: Int,
    val state: String,
    val tags: List<Any>,
    val title: String
)