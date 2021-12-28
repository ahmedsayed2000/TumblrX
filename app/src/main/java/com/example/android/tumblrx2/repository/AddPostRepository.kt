package com.example.android.tumblrx2.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.android.tumblrx2.BlogSearchList
import com.example.android.tumblrx2.addpost.addpostfragments.AddPostViewModel
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


        val tag = MultipartBody.Part.createFormData("tags[0]", "Ammar")

        val call = WebServiceClient().buildApi(AddPostApi::class.java)
            .postToBlog(contentList, tagsList!!, header!!, fileList)
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

    fun searchBlogs(searchBlog: String, context: Context, postViewModel: AddPostViewModel) {

        var list: MutableList<BlogSearch> = mutableListOf()

        val client = WebServiceClient().buildApi(AddPostApi::class.java)
        client.getBlogSearch(searchBlog).enqueue(object: Callback<BlogSearchList>{
            override fun onResponse(
                call: Call<BlogSearchList>,
                response: Response<BlogSearchList>
            ) {

                list = response.body()!!.blogs

                Toast.makeText(
                    context,
                    "response happened with status ${response.code()}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("request", response.message())
                Log.d("response size", "${list.size}")

                postViewModel.searchedBlogs = list
            }

            override fun onFailure(call: Call<BlogSearchList>, t: Throwable) {

                Toast.makeText(context, "response failed", Toast.LENGTH_SHORT).show()
                Log.d("request failed", t.message.toString()!!)
            }
        })



        //return list

    }

    fun searchTags(search: String, context: Context, postViewModel: AddPostViewModel) {
        val client = WebServiceClient().buildApi(AddPostApi::class.java)

        CoroutineScope(IO).launch {
            val job = client.getBlogTags(search).await()
            postViewModel.searchedBlogs = job.blogs
        }

    }



}