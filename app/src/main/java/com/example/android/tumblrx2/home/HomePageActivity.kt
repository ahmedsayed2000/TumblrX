package com.example.android.tumblrx2.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.android.tumblrx2.*
import com.example.android.tumblrx2.databinding.ActivityHomePageBinding
import com.example.android.tumblrx2.intro.IntroActivity
import com.example.android.tumblrx2.network.RetrofitInstance
import kotlinx.coroutines.launch

/**
 * Activity responsible of showing the home page of the user
 */

class HomePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding
    private lateinit var viewModel: HomeViewModel

    private lateinit var adapter: PostAdapter
    private val postList = mutableListOf<PostItem>()
    /**
     * creates the Main layout and renders it
     * @param[savedInstanceState] a Bundle that has the extras sent to this activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = getSharedPreferences("appPref", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_page)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        binding.buttonAddPost.setOnClickListener {
            startActivity(Intent(this, AddPostActivity::class.java))
        }

        lifecycleScope.launch {
            val token = sharedPref.getString("token",null)
            if(token!=null){
                val response = viewModel.getUserInfo(token)
                if(response.isSuccessful){
                    viewModel.userInfo = response.body()!!
                    val name = viewModel.userInfo.name
                    binding.tvTest.text = "Hello $name!"
                }
            }
        }

        binding.bottomNavbar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> {
                    startActivity(Intent(this@HomePageActivity, HomePageActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    return@setOnItemSelectedListener false
                }
                R.id.ic_explore -> {
                    return@setOnItemSelectedListener false
//                    startActivity(Intent(this@HomePageActivity,HomePageActivity::class.java))
//                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                }
                R.id.ic_messages -> {
                    startActivity(
                        Intent(
                            this@HomePageActivity,
                            ActivityAndMessagesActivity::class.java
                        )
                    )
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    return@setOnItemSelectedListener false
                }
                R.id.ic_profile -> {
                    startActivity(Intent(this@HomePageActivity, ActivityBlog::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    return@setOnItemSelectedListener false
                }
                else -> {
                    val editor = sharedPref.edit()
                    editor.clear()
                    editor.apply()
                    startActivity(
                        Intent(
                            this@HomePageActivity, IntroActivity::class.java
                        )
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )

                    return@setOnItemSelectedListener false
                }
            }
        }

        // init the adapter
        adapter = PostAdapter(this, 0, postList)

        // init the listView
        binding.postList.adapter = adapter

        // adding some posts
        postList.add(PostItem("text 1", null, "https://stackoverflow.com", "User 1"))
        postList.add(PostItem("text 2", null, "https://stackoverflow.com", "User 2"))
        postList.add(PostItem("text 3", null, "https://stackoverflow.com", "User 3"))
        postList.add(PostItem("text 4", null, "https://stackoverflow.com", "User 4"))
        postList.add(PostItem("text 5", null, "https://stackoverflow.com", "User 5"))
        postList.add(PostItem("text 6", null, "https://stackoverflow.com", "User 6"))
        postList.add(PostItem("text 7", null, "https://stackoverflow.com", "User 7"))
        // notify the adapter
        adapter.notifyDataSetChanged()
    }
}