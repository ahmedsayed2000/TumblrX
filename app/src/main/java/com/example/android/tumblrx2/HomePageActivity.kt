package com.example.android.tumblrx2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.android.tumblrx2.data.UserPreferences
import com.example.android.tumblrx2.data.repository.UserRepository
import com.example.android.tumblrx2.databinding.ActivityHomePageBinding
import com.example.android.tumblrx2.login.LoginActivity
import com.example.android.tumblrx2.login.LoginViewModel
import com.example.android.tumblrx2.login.LoginViewModelFactory
import com.example.android.tumblrx2.network.Response
import com.example.android.tumblrx2.network.UserApi
import com.example.android.tumblrx2.network.WebServiceClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Activity responsible of showing the home page of the user
 */

class HomePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding
    private lateinit var model: HomeViewModel
    private lateinit var repository: UserRepository
    private lateinit var userPreferences: UserPreferences
    private var webServiceClient = WebServiceClient()

    /**
     * creates the Main layout and renders it
     * @param[savedInstanceState] a Bundle that has the extras sent to this activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_page)

        userPreferences = UserPreferences(this)

        val factory = LoginViewModelFactory(getRepository())

        model = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        lifecycleScope.launch { userPreferences.authToken.first() }

        binding.btTest.setOnClickListener {
            model.getUser()
        }

        model.user.observe(this, Observer {
            when (it) {
                is Response.Success -> {
                    Log.i("HomePageActivity", it.value.toString())
                    if(it.value != null) {
                        binding.tvTest.text = "Hello ${it.value}"
                    }
                    else{
                        binding.tvTest.text = "NO USER FOUND"
                    }
                }
                is Response.Failure->{
                    Log.i("HomePageActivity", it.toString())
                }
            }

        })


        binding.buttonAddPost.setOnClickListener {
            startActivity(Intent(this, AddPostActivity::class.java))
        }
    }

    private fun getRepository(): UserRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = webServiceClient.buildApi(UserApi::class.java, token)
        return UserRepository(api)
    }
}