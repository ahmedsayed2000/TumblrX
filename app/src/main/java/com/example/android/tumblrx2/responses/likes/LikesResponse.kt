package com.example.android.tumblrx2.responses.likes

data class LikesResponse(
    val likePosts: List<LikePost>,
    val likedCount: Int
)