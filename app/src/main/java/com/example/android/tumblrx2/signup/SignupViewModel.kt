package com.example.android.tumblrx2.signup

import androidx.lifecycle.ViewModel
import com.example.android.tumblrx2.responses.RegisterResponse
import com.example.android.tumblrx2.network.RetrofitInstance
import retrofit2.Response

class SignupViewModel : ViewModel() {


    /**
     * returns a code to specify if either [email], [password], [username], or [age] fields are invalid
     */
    fun validateInput(email: String, password: String, username: String, age: String): Int {
        val usernameRegex = Regex("""^[A-Za-z0-9-]*$""")
        return if (email.isEmpty() || password.isEmpty() || username.isEmpty() || age.isEmpty()) -1
        else if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) 1
        else if (password.length < 6) 2
        else if (username.length > 32 || !usernameRegex.containsMatchIn(username)) 3
        else if (age.toInt() < 13 || age.toInt() > 130) 4
        else 0
    }

    /**
     * returns a suitable error message according to the [errCode]
     */
    fun chooseErrMsg(errCode: Int): String {
        return when (errCode) {
            -1 -> "Make sure all fields are filled"
            1 -> "Enter a correct email"
            2 -> "Password is too short"
            3 -> "Username is invalid, make sure it is less than 32 characters, and only contains letter, numbers, or dashes (-)"
            else -> "Enter a valid age between 13 and 130"
        }
    }

    suspend fun signup(
        email: String,
        password: String,
        username: String
    ): Response<RegisterResponse> {
        return RetrofitInstance.api.signup(email, username, password)
    }

}