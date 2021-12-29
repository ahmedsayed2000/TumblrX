package com.example.android.tumblrx2.responses.dashboarddata

data class ForYouPost(
    val __v: Int,
    val _id: String,
    val blogAttribution: BlogAttribution,
    val commentsCount: Int,
    val content: List<Content>,
    val id: String,
    val isReblogged: Boolean,
    val liked: Boolean,
    val likesCount: Int,
    val notes: String,
    val notesCount: Int,
    val postType: String,
    val publishedOn: Int,
    val reblogsCount: Int,
    val state: String,
    val tags: List<String>,
    val title: String,
    val trail: List<Trail>
)