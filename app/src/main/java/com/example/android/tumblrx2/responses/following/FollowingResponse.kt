package com.example.android.tumblrx2.responses.following

data class FollowingResponse(
    val followingBlogs: List<FollowingBlog>,
    val numberOfFollowing: Int
)