package com.example.android.tumblrx2.login

data class User(
//    val access_token: String?,
    val blogs: List<String>,
    val default_post_format: String,
    val email: String,
    val following: Int,
    val likes: Int,
    val name: String,
    val primary_blog: String,
    val settings: Settings
)
//{
//    constructor(access_token: String?) : this(
//        access_token,
//        listOf(""),
//        "",
//        "",
//        -1,
//        -1,
//        "",
//        "",
//        Settings(false,false,true,false,false,true,true)
//    )
//}