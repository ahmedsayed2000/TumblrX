package com.example.android.tumblrx2

import com.example.android.tumblrx2.postobjects.PostObject
import com.google.gson.Gson


class JsonClient(val parseJson: ParseJsonService) {


    public fun getJson (list: List<PostObject>): String {
        val content = parseJson.parse(list)
        val jsonString = Gson().toJson(content)
        return jsonString
    }
}