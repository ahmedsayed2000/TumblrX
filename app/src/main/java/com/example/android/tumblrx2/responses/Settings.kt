package com.example.android.tumblrx2.responses

data class Settings(
    val activatePassward: Boolean,
    val allowAsk: Boolean,
    val allowSubmission: Boolean,
    val blogAvatar: Boolean,
    val hideFromSearch: Boolean,
    val messaging: Boolean,
    val replies: Boolean,
    val shareFollowing: Boolean,
    val shareLikes: Boolean
)