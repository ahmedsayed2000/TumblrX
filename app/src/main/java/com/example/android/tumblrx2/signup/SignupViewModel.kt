package com.example.android.tumblrx2.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignupViewModel : ViewModel() {

    /**
     * returns a code to specify if either [email], [password], [username], or [age] fields are invalid
     */
    fun validateInput(email: String, password: String, username: String, age: String): Int {
        return if (email.isEmpty() || password.isEmpty() || username.isEmpty() || age.isEmpty()) -1
        else if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) 1 //crashes
//        return if(email.length < 1) 1
        else if (password.length < 6) 2
        else if (username.length < 6) 3
        else if (age.toInt() < 1) 4
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
            3 -> "Username length is too short"
            else -> "Enter a valid age"
        }
    }

}