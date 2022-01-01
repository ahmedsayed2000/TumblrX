package com.example.android.tumblrx2.addpost.addpostfragments


/**
 * this class holds the blogs handle and title that are searched for in the tags
 * @property[title] the blog title
 * @property[handle] the blog unique name or the handle
 */
data class BlogSearch(
    val title: String,
    val handle: String
)