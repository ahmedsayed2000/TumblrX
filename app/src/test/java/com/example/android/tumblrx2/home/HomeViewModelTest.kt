package com.example.android.tumblrx2.home

import com.example.android.tumblrx2.responses.InfoResponse
import com.example.android.tumblrx2.responses.RegisterResponse
import com.example.android.tumblrx2.responses.Settings
import com.example.android.tumblrx2.responses.dashboardpost.DashboardPost
import com.google.common.truth.Truth
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import retrofit2.Response

class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        viewModel = HomeViewModel()
    }

    @Test
    fun getUserInfoResponseSuccess() {
        val response: Response<InfoResponse> = Response.success(
            InfoResponse(
                emptyList(),
                "text",
                "mock@mock.com",
                0,
                "mockid",
                0,
                "mockName",
                "mockBlog",
                Settings(false, false, false, false, false, false, false)
            )
        )
        Truth.assertThat(response.isSuccessful).isTrue()
    }

    @Test
    fun getUserInfoResponseFailure(){
        val errorResponse =
            "{\n" +
                    "  \"type\": \"error\",\n" +
                    "  \"message\": \"Something went wrong.\"\n" + "}"
        val errorResponseBody = errorResponse.toResponseBody("application/json".toMediaTypeOrNull())
        val response : Response<InfoResponse> = Response.error(400,errorResponseBody)
        Truth.assertThat(response.isSuccessful).isFalse()
    }

    @Test
    fun getDashboardPostsResponseSuccess() {
        val response: Response<DashboardPost> = Response.success(DashboardPost(ArrayList()))
        Truth.assertThat(response.isSuccessful).isTrue()
    }

    @Test
    fun getDashboardPostsResponseFailure(){
        val errorResponse =
            "{\n" +
                    "  \"type\": \"error\",\n" +
                    "  \"message\": \"Something went wrong.\"\n" + "}"
        val errorResponseBody = errorResponse.toResponseBody("application/json".toMediaTypeOrNull())
        val response : Response<DashboardPost> = Response.error(400,errorResponseBody)
        Truth.assertThat(response.isSuccessful).isFalse()
    }

}