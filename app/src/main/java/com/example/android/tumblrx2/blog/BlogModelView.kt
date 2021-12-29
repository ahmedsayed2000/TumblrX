package com.example.android.tumblrx2.login

import androidx.lifecycle.*
import com.example.android.tumblrx2.responses.LoginResponse
import com.example.android.tumblrx2.network.RetrofitInstance
import com.example.android.tumblrx2.responses.CreateBlogResponse
import retrofit2.Response

class BlogModelView() : ViewModel() {
    fun validateInput(title: String): Int {
        return if (title.isEmpty()) -1
        else 0
    }

    fun chooseErrMsg(errCode: Int): String {
        return when (errCode) {
            -1 -> "Blog Name Is Required"
            else -> ""
        }
    }

    suspend fun createBlog(title: String): Response<CreateBlogResponse> {
        val header="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MWIyMTMxZDBiOGFhZWM2MGM1YWYwZWIiLCJwcmltYXJ5QmxvZyI6IjYxYjIxMzFkMGI4YWFlYzYwYzVhZjBlZSIsImlhdCI6MTY0MDU0MzAwMX0.LZaUj3Xd04PKSUxlzma1FV-jVxmXzK1In8BZbfFappA"
        return RetrofitInstance.api.createBlog(header,title,title,false)
    }
}