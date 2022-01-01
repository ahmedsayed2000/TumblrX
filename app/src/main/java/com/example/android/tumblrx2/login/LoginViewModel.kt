package com.example.android.tumblrx2.login

import androidx.lifecycle.*
import com.example.android.tumblrx2.responses.LoginResponse
import com.example.android.tumblrx2.network.RetrofitInstance
import retrofit2.Response


class LoginViewModel() : ViewModel() {

    /**
     * performs client-side validation on the given [email] and [password]
     * and returns a suitable result code
     */
    fun loginValidateInput(email: String, password: String): Int {
        return if (email.isEmpty() || password.isEmpty()) -1
        else if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) 1
//        else if (email.length < 5) 1
        else if (password.length < 6) 2
        else 0
    }

    /**
     * returns a suitable error message according the the given [errCode]
     */
    fun loginChooseErrMsg(errCode: Int): String {
        return when (errCode) {
            -1 -> "Make sure all fields are filled"
            1 -> "Enter a valid email"
            else -> "Password is too short"
        }
    }

    /**
     * uses the Retrofit instance function login to hit the backend and login the user
     * returns a response object which is parsed in the activity and if successful, contains the status and token of the user
     * known through the given [email] and [password]
     */
    suspend fun login(email: String, password: String): Response<LoginResponse> {
        return RetrofitInstance.api.login(email, password)
    }
}