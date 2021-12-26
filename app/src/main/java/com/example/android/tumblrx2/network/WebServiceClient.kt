package com.example.android.tumblrx2.network

import com.example.android.tumblrx2.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class WebServiceClient {
    companion object {
        //        private const val BASE_URL = "http://10.0.2.2:4000/"  //localhost backend - only works on emulator
        private const val BASE_URL =
            "http://tumblrx.me:3000/" //deployed backend hostname
//        private const val BASE_URL = "http://52.190.57.101:3000/" //deployed backend ip address
    }

    fun <Api> buildApi(
        api: Class<Api>
    ): Api {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder().also { client ->
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                    client.connectTimeout(5, TimeUnit.SECONDS)
                }.build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }
}