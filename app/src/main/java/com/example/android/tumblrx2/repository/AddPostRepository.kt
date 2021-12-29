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


        val sharedPref = context.getSharedPreferences("appPref", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)
        if (token != null) {
            Log.i("LoginActivity", token!!)
        }

        val header = token


        val part = MultipartBody.Part.createFormData("content[0][type]", "text")

        val part1 = MultipartBody.Part.createFormData("content[0][text]", "ahmed")
        val part2 = MultipartBody.Part.createFormData("content[1][type]", "image")
        val part3 = MultipartBody.Part.createFormData("content[1][identifier]", "image1")


        val call = WebServiceClient().buildApi(AddPostApi::class.java)
            .postToBlog(contentList, header!!, fileList)
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