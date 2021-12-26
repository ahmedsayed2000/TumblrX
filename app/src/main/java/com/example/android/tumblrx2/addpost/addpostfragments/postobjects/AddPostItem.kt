package com.example.android.tumblrx2.addpost.addpostfragments.postobjects

import android.widget.MediaController
import androidx.core.view.GestureDetectorCompat
import com.giphy.sdk.core.models.Media

data class AddPostItem(val type: Int, var content: String) {

    var mediaController: MediaController? = null
    var giphMedia: Media? = null
    var textGestureDetector: GestureDetectorCompat? = null
}