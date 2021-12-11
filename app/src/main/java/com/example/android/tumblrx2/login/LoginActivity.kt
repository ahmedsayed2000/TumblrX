package com.example.android.tumblrx2.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.tumblrx2.HomePageActivity
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

        binding.etEmail.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tvErrMsg.visibility = View.GONE
                binding.tvMissingFieldMsg.visibility = View.GONE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun afterTextChanged(s: Editable?) {
                return
            }
        })

        binding.etPassword.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tvErrMsg.visibility = View.GONE
                binding.tvMissingFieldMsg.visibility = View.GONE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun afterTextChanged(s: Editable?) {
                return
            }
        })

        model.loginResponse.observe(this, Observer { it ->
            when (it) {
                is Response.Success -> {
                    binding.tvErrMsg.visibility = View.GONE
                    Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
                    Log.i("LoginActivity", it.toString())
                    startActivity(Intent(this, HomePageActivity::class.java))
                }
                is Response.Failure -> {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                    Log.i("LoginActivity", it.toString())
                    binding.tvErrMsg.visibility = View.VISIBLE
                }
            }
        })
        setContentView(binding.root)
        binding.actionbarLogin.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                binding.tvMissingFieldMsg.visibility = View.VISIBLE
            } else model.login(email, password)
        }

        binding.actionbarLogin.btnBack.setOnClickListener {
            finish()
        }
    }

}