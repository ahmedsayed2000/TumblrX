package com.example.android.tumblrx2.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.tumblrx2.network.Response
import com.example.android.tumblrx2.repository.LoginSignupRepository
import com.example.android.tumblrx2.responses.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginSignupRepository) : ViewModel() {
    private val _loginResponse: MutableLiveData<Response<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Response<LoginResponse>>
        get() = _loginResponse

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginResponse.value = repository.login(email, password)
    }
}