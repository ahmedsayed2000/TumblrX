package com.example.android.tumblrx2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.tumblrx2.data.repository.UserRepository
import com.example.android.tumblrx2.data.responses.LoginResponse
import com.example.android.tumblrx2.login.User
import com.example.android.tumblrx2.network.Response
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: UserRepository
) : ViewModel() {
    private val _user: MutableLiveData<Response<User>> = MutableLiveData()
    val user: LiveData<Response<User>>
        get() = _user

    /**
     * Uses the UserRepository to hit the backend and return  user info
     */
    fun getUser() = viewModelScope.launch {
        _user.value = repository.getUser()
    }
}