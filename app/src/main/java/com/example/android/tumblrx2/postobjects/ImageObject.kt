package com.example.android.tumblrx2.postobjects

import com.google.gson.annotations.SerializedName

import java.io.File

class ImageObject : PostObject {
    override val type: String
        get() = "image"

    @SerializedName("type")
    var mType = type
    var width: Int = 0
    var height: Int = 0
    var file:File? = null
}