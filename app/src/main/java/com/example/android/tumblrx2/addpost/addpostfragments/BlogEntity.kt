package com.example.android.tumblrx2.addpost.addpostfragments


/**
 * this class holds the user Blog info
 * @property[_id] blog id
 * @property[description] blog description
 * @property[handle] blog handle
 * @property[headerImage] blog header image url
 * @property[title] blog title
 * @property[isPrivate] a boolean represents the privacy of the blog
 */
data class BlogEntity(
    val _id: String,
    val askAnon: Boolean,
    val description: String,
    val handle: String,
    val headerImage: String,
    val isAvatarCircle: Boolean,
    val isPrimary: Boolean,
    val isPrivate: Boolean,
    val title: String
)