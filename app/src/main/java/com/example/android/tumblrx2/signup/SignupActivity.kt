package com.example.android.tumblrx2.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.android.tumblrx2.databinding.ActivitySignupBinding


class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var viewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[SignupViewModel::class.java]
        setContentView(binding.root)

//        setContentView(R.layout.activity_signup)

        binding.etEmail.addTextChangedListener(inputWatcher)
        binding.etPassword.addTextChangedListener(inputWatcher)
        binding.etUsername.addTextChangedListener(inputWatcher)
        binding.etAge.addTextChangedListener(inputWatcher)

        binding.btnSignup.setOnClickListener {
            //call function to check inputs, if there is an error, call displayErr
            val emailText = binding.etEmail.text.trim().toString()
            val passwordText = binding.etEmail.text.trim().toString()
            val usernameText = binding.etEmail.text.trim().toString()
            val ageText = binding.etEmail.text.trim().toString()

            val code = viewModel.validateInput(emailText, passwordText, usernameText, ageText)

            if (code != 0) displayErr(viewModel.chooseErrMsg(code))
            // else signup function yay

        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private val inputWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            if (binding.tvErrMsg.visibility == View.VISIBLE) binding.tvErrMsg.visibility = View.GONE
        }
    }

    /**
     * Shows an error message with [errMsg]
     */
    fun displayErr(errMsg: String) {
        binding.tvErrMsg.text = errMsg
        binding.tvErrMsg.visibility = View.VISIBLE
    }

}