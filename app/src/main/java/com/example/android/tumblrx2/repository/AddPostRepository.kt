package com.example.android.tumblrx2.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.android.tumblrx2.network.AddPostApi
import com.example.android.tumblrx2.network.WebServiceClient
import com.example.android.tumblrx2.addpost.addpostfragments.postobjects.*
import com.example.android.tumblrx2.responses.LoginResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response

class AddPostRepository() {

    fun postToBlog(
        context: Context,
        contentList: MutableList<MultipartBody.Part>,
        fileList: MutableList<MultipartBody.Part>?
    ) {

        val header =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MWMxZTNiODZiODI3YTdlMTQ0NDU4ZjYiLCJwcmltYXJ5QmxvZyI6IjYxYzFlM2I4NmI4MjdhN2UxNDQ0NThmOSIsImlhdCI6MTY0MDM4MTUzMH0.WhorTIkfmeGWOMgWXi55HTc8EEu57O2Jej3Sl__JF2g"

        //val postEntity = PostEntity(null, null, null, postObjects)

        //val postString = Gson().toJson(PostContent(postObjects))
        //val requestBody = postString.toRequestBody("text/*".toMediaTypeOrNull())

        //val part = MultipartBody.Part.createFormData("content", postString)
        val part = MultipartBody.Part.createFormData("content[0][type]", "text")

        val part1 = MultipartBody.Part.createFormData("content[0][text]", "ahmed")
        val part2 = MultipartBody.Part.createFormData("content[1][type]", "image")
        val part3 = MultipartBody.Part.createFormData("content[1][identifier]", "image1")

        //Log.d("post string", postString)

        //val postPart = MultipartBody.Part.createFormData("postSequence", postString)

        val call = WebServiceClient().buildApi(AddPostApi::class.java)
            .postToBlog(contentList, header, fileList)
        call.enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Toast.makeText(
                    context,
                    "response happened with status ${response.code()}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("request", response.message())
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(context, "response failed", Toast.LENGTH_SHORT).show()
                Log.d("request failed", t.message.toString()!!)
            }
        }
        )
    }


}