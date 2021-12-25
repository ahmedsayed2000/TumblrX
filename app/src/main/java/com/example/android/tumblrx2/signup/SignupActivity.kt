package com.example.android.tumblrx2.signup

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.example.android.tumblrx2.databinding.ActivitySignupBinding
import com.example.android.tumblrx2.signup.network.RegisterResponse
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException


class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var viewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[SignupViewModel::class.java]
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("appPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
//        setContentView(R.layout.activity_signup)

        binding.etEmail.addTextChangedListener(inputWatcher)
        binding.etPassword.addTextChangedListener(inputWatcher)
        binding.etUsername.addTextChangedListener(inputWatcher)
        binding.etAge.addTextChangedListener(inputWatcher)

        binding.btnSignup.setOnClickListener {
            binding.progressSignup.visibility = View.VISIBLE

            //call function to check inputs, if there is an error, call displayErr
            val emailText = binding.etEmail.text.trim().toString()
            val passwordText = binding.etEmail.text.trim().toString()
            val usernameText = binding.etEmail.text.trim().toString()
            val ageText = binding.etEmail.text.trim().toString()

            val code = viewModel.validateInput(emailText, passwordText, usernameText, ageText)

            if (code != 0) displayErr(viewModel.chooseErrMsg(code))
            else {
                lifecycleScope.launchWhenCreated {
                    val response: Response<RegisterResponse> = try {
                        viewModel.signup(emailText, passwordText, usernameText)
                    } catch (e: IOException) {
                        displayErr("You might not have internet connection")
                        return@launchWhenCreated
                    } catch (e: HttpException) {
                        displayErr(e.toString())
                        return@launchWhenCreated
                    }
                    if(response.isSuccessful){
                        Log.i("SignupActivity", "Success")
                        val token: String? = response.body()?.token
                        editor.apply {
                            putString("token", token)
                        }
                    }
                }
            }
            binding.progressSignup.visibility=View.GONE
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