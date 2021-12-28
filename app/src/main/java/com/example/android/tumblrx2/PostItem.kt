package com.example.android.tumblrx2

import android.graphics.Bitmap

data class PostItem(
    val text: String?,
    val image: Bitmap?,
    val link: String?,
    val blogName: String
) {
}