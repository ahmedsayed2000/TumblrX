package com.example.android.tumblrx2.addpost.addpostfragments.postobjects

import android.widget.MediaController
import androidx.core.view.GestureDetectorCompat
import com.giphy.sdk.core.models.Media


/**
 * this class holds the data of each post item and is used in the AddPostAdapter class
 * @property[type] the type of the item such as Text, image, video, link, gif...etc
 * @property[content] the content of the item
 */
data class AddPostItem(val type: Int, var content: String) {

    var mediaController: MediaController? = null
    var giphMedia: Media? = null
    var textGestureDetector: GestureDetectorCompat? = null
}