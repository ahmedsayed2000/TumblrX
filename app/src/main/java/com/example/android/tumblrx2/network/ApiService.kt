package com.example.android.tumblrx2.network

import com.example.android.tumblrx2.responses.CreateBlogResponse
import com.example.android.tumblrx2.responses.InfoResponse
import com.example.android.tumblrx2.responses.LoginResponse
import com.example.android.tumblrx2.responses.RegisterResponse
import com.example.android.tumblrx2.responses.chatsresponse.ChatsResponse
import com.example.android.tumblrx2.responses.dashboarddata.DashboardResponse
import com.example.android.tumblrx2.responses.dashboardpost.DashboardPost
import com.example.android.tumblrx2.responses.following.FollowingResponse
import com.example.android.tumblrx2.responses.likes.LikesResponse
import com.example.android.tumblrx2.responses.messages.MessagesResponse
import com.example.android.tumblrx2.responses.postsdata.PostsResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

/**
 * functions of this interface hit the backend and are implemented in the viewModel classes
 */
interface ApiService {

    /**
     * adds a new user to the database with the given [email], [username], and [password]
     * returns a response object from the backend
     */
    @FormUrlEncoded
    @POST("api/user/register")
    suspend fun signup(
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<RegisterResponse>

    /**
     * logs in the user specified by the given [email] and [password]
     * returns a response object with the status of this user
     */
    @FormUrlEncoded
    @POST("api/user/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("/api/blog/{id}")
    suspend fun createBlog(
        @Header("Authorization") header: String,
        @Field("title") title: String,
        @Field("handle") handle: String,
        @Field("private") private: Boolean,
        @Path("id") id:String,
        ): Response<CreateBlogResponse>

    /**
     * retrieves info about the user specified by the given [token]
     * returns a response object containing info from the backend if successful
     */
    @GET("api/user/info")
    suspend fun getInfo(
        @Header("Authorization") token: String
    ): Response<InfoResponse>

    /**
     * retrieves posts relating to the user specified by the given [token]
     * returns a response object containing a list of posts if successful
     */
    @GET("api/user/dashboard")
    suspend fun getDashboardPosts(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 10, @Query("page") page: Int = 1
    ): Response<DashboardPost>

    @GET("api/blog/{id}/posts")
    suspend fun getBlogPosts(
        @Header("Authorization") token: String,
        @Path("id") id:String,
    ): Response<PostsResponse>

    @GET("api/user/chat/reterive-conversations")
    suspend fun getChats(
        @Header("Authorization") token: String
    ): Response<ChatsResponse>

    @GET("api/user/chat/reterive-chat/{id}")
    suspend fun getChatsMessages(
        @Header("Authorization") token: String,
        @Path("id") id:String,
        ): Response<MessagesResponse>

    @GET("api/user/following")
    suspend fun getFollowing(
        @Header("Authorization") token: String
    ): Response<FollowingResponse>

    @GET("api/user/likes")
    suspend fun getLikes(
        @Header("Authorization") token: String
    ): Response<LikesResponse>


}