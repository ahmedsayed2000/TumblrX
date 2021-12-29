package com.example.android.tumblrx2.responses

data class CustomApperance(
    val customParameters: CustomParameters,
    val customTheme: String,
    val enableMobileInterFace: Boolean,
    val globalParameters: GlobalParameters,
    val openLinksInNewWindow: Boolean,
    val truncateFeed: Boolean
)