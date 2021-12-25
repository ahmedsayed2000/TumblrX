package com.example.android.tumblrx2.login

import androidx.lifecycle.*
import com.example.android.tumblrx2.repository.LoginSignupRepository
import com.example.android.tumblrx2.responses.LoginResponse
import com.example.android.tumblrx2.signup.network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel() : ViewModel() {
    fun loginValidateInput(email: String, password: String): Int {
        return if (email.isEmpty() || password.isEmpty()) -1
        else if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) 1
        else if (password.length < 6) 2
        else 0
    }

    fun loginChooseErrMsg(errCode: Int): String {
        return when (errCode) {
            -1 -> "Make sure all fields are filled"
            1 -> "Enter a valid email"
            else -> "Password is too short"
        }
    }

    suspend fun login(email: String, password: String): Response<LoginResponse> {
        return RetrofitInstance.api.login(email, password)
    }
}