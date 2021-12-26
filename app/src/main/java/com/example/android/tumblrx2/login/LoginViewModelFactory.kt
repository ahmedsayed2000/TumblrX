package com.example.android.tumblrx2.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.tumblrx2.repository.BaseRepository
import com.example.android.tumblrx2.repository.LoginSignupRepository
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(private val repository: BaseRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
//            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repository as LoginSignupRepository) as T
            else -> throw IllegalArgumentException("ViewModelClass not found")
        }
    }
}