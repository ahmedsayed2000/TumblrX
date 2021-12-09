package com.example.android.tumblrx2.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.tumblrx2.controllers.LoginController
import com.example.android.tumblrx2.databinding.ActivityLoginBinding
import com.example.android.tumblrx2.network.LoginSignupAPI
import com.example.android.tumblrx2.network.Response
import com.example.android.tumblrx2.network.WebServiceClient
import com.example.android.tumblrx2.repository.LoginSignupRepository
import com.example.android.tumblrx2.responses.LoginResponse
import okhttp3.ResponseBody

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var model: LoginViewModel
    private lateinit var repository: LoginSignupRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val webServiceClient = WebServiceClient()
        repository = LoginSignupRepository(webServiceClient.buildApi(LoginSignupAPI::class.java))
        val factory = LoginViewModelFactory(repository)
        model = ViewModelProvider(this, factory).get(LoginViewModel::class.java)


        model.loginResponse.observe(this, Observer { it ->
            when (it) {
                is Response.Success -> {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                    Log.i("LoginActivity", it.toString())
                }
                is Response.Failure -> {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                    Log.i("LoginActivity", it.toString())
                }
            }
        })
        setContentView(binding.root)
        binding.actionbarLogin.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            model.login(email, password)
        }

//        val controller = LoginController
//
//        binding.actionbarLogin.btnLogin.setOnClickListener {
//            val email = binding.etEmail.text.toString().trim()
//            val password = binding.etPassword.text.toString().trim()
//            val loginResult = controller.onLogin(email, password)
////            if (loginResult != "Success") {
//            Toast.makeText(this, loginResult, Toast.LENGTH_SHORT).show()
////            }
//        }

        binding.actionbarLogin.btnBack.setOnClickListener {
            finish()
        }
    }

}