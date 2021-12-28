package com.example.android.tumblrx2.responses.dashboarddata

import com.google.gson.annotations.SerializedName

data class DashboardResponse(
//    @Json(name = "for-youPosts") val forYouPosts: List<ForYouPost>
    @SerializedName("for-youPosts") val forYouPosts: List<ForYouPost>
)
