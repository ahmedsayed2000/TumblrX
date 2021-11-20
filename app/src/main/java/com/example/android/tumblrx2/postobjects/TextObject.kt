package com.example.android.tumblrx2.postobjects

import com.google.gson.annotations.SerializedName

class TextObject : PostObject {

    override val type: String
        get() = "text"

    @SerializedName("type")
    val mType: String = type

    var text: String? = null
}