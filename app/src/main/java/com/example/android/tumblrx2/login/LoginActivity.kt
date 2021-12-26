//resources:
//https://www.youtube.com/playlist?list=PLk7v1Z2rk4hgmIvyw8rvpiEQxIAbJvDAF
//https://classroom.udacity.com/courses/ud9012 lessons 5,8
package com.example.android.tumblrx2.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.android.tumblrx2.home.HomePageActivity
import com.example.android.tumblrx2.databinding.ActivityLoginBinding
import com.example.android.tumblrx2.responses.LoginResponse
import retrofit2.HttpException
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        val sharedPref = this.getSharedPreferences("appPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        binding.etEmail.addTextChangedListener(inputWatcher)
        binding.etPassword.addTextChangedListener(inputWatcher)

        binding.actionbarLogin.btnLogin.setOnClickListener {
            binding.progressLogin.visibility = View.VISIBLE
            val emailText = binding.etEmail.text.trim().toString()
            val passwordText = binding.etPassword.text.trim().toString()
            val code = viewModel.loginValidateInput(emailText, passwordText)
            if (code != 0) {
                displayErr(viewModel.loginChooseErrMsg(code))
            } else {
                lifecycleScope.launchWhenCreated {
                    val response: retrofit2.Response<LoginResponse> = try {
                        viewModel.login(emailText, passwordText)
                    } catch (e: IOException) {
                        displayErr("You might not have internet connection")
                        return@launchWhenCreated
                    } catch (e: HttpException) {
                        displayErr(e.toString())
                        return@launchWhenCreated
                    }
                    if (response.isSuccessful) {
                        Log.i("LoginActivity", "Success")
                        val token: String? = response.body()?.token
                        editor.apply {
                            putString("token", token)
                        }.apply()
                        Log.i("LoginActivity", "Token: $token")
                        startActivity(Intent(this@LoginActivity, HomePageActivity::class.java).
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                    }else{
                        displayErr("Email and Password do not match")
                    }
                }
            }
            binding.progressLogin.visibility = View.GONE
        }
        binding.actionbarLogin.btnBack.setOnClickListener {
            finish()
        }
    }


    private val inputWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (binding.tvErrMsg.visibility == View.VISIBLE) binding.tvErrMsg.visibility = View.GONE
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    fun displayErr(errMsg: String) {
        binding.tvErrMsg.text = errMsg
        binding.tvErrMsg.visibility = View.VISIBLE
    }
}
