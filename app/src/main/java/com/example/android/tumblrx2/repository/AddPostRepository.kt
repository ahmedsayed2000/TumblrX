package com.example.android.tumblrx2.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.android.tumblrx2.BlogSearchList
import com.example.android.tumblrx2.addpost.addpostfragments.AddPostViewModel
import com.example.android.tumblrx2.addpost.addpostfragments.BlogEntity
import com.example.android.tumblrx2.addpost.addpostfragments.BlogSearch
import com.example.android.tumblrx2.network.AddPostApi
import com.example.android.tumblrx2.network.WebServiceClient
import com.example.android.tumblrx2.addpost.addpostfragments.postobjects.*
import com.example.android.tumblrx2.responses.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPostRepository() {

    fun postToBlog(
        context: Context,
        postViewModel: AddPostViewModel,
        id: String,
        postType: String,
        contentList: MutableList<MultipartBody.Part>,
        fileList: MutableList<MultipartBody.Part>?,
        tagsList: MutableList<MultipartBody.Part>?
    ) {


        val sharedPref = context.getSharedPreferences("appPref", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)
        if (token != null) {
            Log.i("LoginActivity", token!!)
        }

        val header = token

        val type = MultipartBody.Part.createFormData("state", postType)

        val call = WebServiceClient().buildApi(AddPostApi::class.java)
            .postToBlog(id, type, contentList, tagsList!!, header!!, fileList)
        call.enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Toast.makeText(
                    context,
                    "posted to the server",
                    Toast.LENGTH_SHORT
                ).show()
                postViewModel.finishPost.value = true
                Log.d("request", response.message())
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(context, "response failed", Toast.LENGTH_SHORT).show()
                Log.d("request failed", t.message.toString()!!)
            }
        }
        )
    }

    fun searchBlogs(searchBlog: String): Call<BlogSearchList> {

        val client = WebServiceClient().buildApi(AddPostApi::class.java)

        return client.getTagSearch(searchBlog)


    }



    fun getBlogs(context: Context): Call<MutableList<BlogEntity>> {
        val sharedPref = context.getSharedPreferences("appPref", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)
        if (token != null) {
            Log.i("LoginActivity", token!!)
        }

        val header = token

        val client = WebServiceClient().buildApi(AddPostApi::class.java)

        val call = client.getBlogs(header!!)

        return call
    }





}