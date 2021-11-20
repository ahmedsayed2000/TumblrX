package com.example.android.tumblrx2.controllers

import android.content.Context
import com.example.android.tumblrx2.utils.User


//class LoginController(val context: Context) {
object LoginController {
    private var mockUsers: MutableList<User> =
        mutableListOf(User("admin@admin", "admin"), User("Ahmed@gmail.com", "123456"))

    /**
     * uses the User.validateUser function to return an appropriate message to LoginActivity to be displayed.
     *
     * @param email the string entered in the email editText
     * @param password the password entered in the password editText
     * @return a string with the message to be displayed to the user
     */
    fun onLogin(email: String?, password: String?): String {
        val user = User(
            email!!,
            password!!
        )
        println(mockUsers.size)
        when (user.validateUser(email, password)) {
            1 -> {
                return "Please enter an email"
            }
            2 -> {
                return "Please enter a valid email"
            }
            3 -> {
                return "Please enter a password"
            }
            else -> {
                mockUsers.forEach { mockUser ->
                    if (mockUser.email == user.email && mockUser.password == user.password) return "Success"
                }
            }
        }
        return "Email and Password do not match"
    }
}