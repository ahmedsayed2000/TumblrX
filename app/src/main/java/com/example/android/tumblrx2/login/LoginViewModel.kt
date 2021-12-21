package com.example.android.tumblrx2.login

import androidx.lifecycle.*
import com.example.android.tumblrx2.network.Response
import com.example.android.tumblrx2.data.repository.LoginSignupRepository
import com.example.android.tumblrx2.data.responses.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginSignupRepository) : ViewModel() {
    private val _loginResponse: MutableLiveData<Response<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Response<LoginResponse>>
        get() = _loginResponse

    private val _emailInput: MutableLiveData<String> = MutableLiveData()
    val emailInput: LiveData<String>
        get() = _emailInput

    private val _passwordInput: MutableLiveData<String> = MutableLiveData()
    val passwordInput: LiveData<String>
        get() = _passwordInput

//    val _loginMediator = MediatorLiveData<Boolean>()
//
//    init {
//        _loginMediator.addSource(_emailInput) { validateForm() }
//        _loginMediator.addSource(_passwordInput) { validateForm() }
//    }
//
//    private fun validateForm(): Boolean {
//        if (_emailInput.toString().isEmpty() || _passwordInput.toString().isEmpty()) return false
//        return true
//    }
//
//    override fun onCleared() {
//        _loginMediator.removeSource(_emailInput)
//        _loginMediator.removeSource(_passwordInput)
//    }


    fun login(email: String, password: String) = viewModelScope.launch {
        _loginResponse.value = repository.login(email, password)
    }

    /**
     * calls the saveAuthToken function in the LoginRepository class with [token]
     */
    fun saveAuthToken(token: String) = viewModelScope.launch {
        repository.saveAuthToken(token)
    }
}