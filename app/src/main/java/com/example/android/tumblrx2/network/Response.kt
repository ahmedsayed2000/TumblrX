package com.example.android.tumblrx2.network

import okhttp3.ResponseBody


sealed class Response<out T> {
    data class Success<out T>(val value: T) : Response<T>()
    data class Failure(
        val isNetworkError: Boolean?,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ) : Response<Nothing>()
}