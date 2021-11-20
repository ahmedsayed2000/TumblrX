package com.example.android.tumblrx2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.android.tumblrx2.controllers.LoginController
import com.example.android.tumblrx2.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val controller = LoginController

        binding.actionbarLogin.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val loginResult = controller.onLogin(email, password)
//            if (loginResult != "Success") {
            Toast.makeText(this, loginResult, Toast.LENGTH_SHORT).show()
//            }
        }
        binding.actionbarLogin.btnBack.setOnClickListener {
            finish()
        }
    }

}