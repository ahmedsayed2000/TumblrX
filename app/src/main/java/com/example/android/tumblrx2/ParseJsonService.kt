package com.example.android.tumblrx2

import com.example.android.tumblrx2.postobjects.PostContent
import com.example.android.tumblrx2.postobjects.PostObject

interface ParseJsonService {

    fun parse(list: List<PostObject>): PostContent

}