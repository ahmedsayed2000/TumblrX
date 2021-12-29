package com.example.android.tumblrx2.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.tumblrx2.*
import com.example.android.tumblrx2.activity.ActivityAndMessagesActivity
import com.example.android.tumblrx2.blog.ActivityBlog
import com.example.android.tumblrx2.databinding.ActivityHomePageBinding
import com.example.android.tumblrx2.intro.IntroActivity
import kotlinx.coroutines.launch

/**
 * Activity responsible of showing the home page of the user
 */

class HomePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding
    private lateinit var viewModel: HomeViewModel

    private lateinit var adapter: PostAdapter
    private val postList = mutableListOf<PostItem>()

    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: RecyclerView.Adapter<*>

    /**
     * creates the Main layout and renders it
     * @param[savedInstanceState] a Bundle that has the extras sent to this activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = getSharedPreferences("appPref", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_page)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        manager = LinearLayoutManager(this)

        binding.buttonAddPost.setOnClickListener {
            var primaryBlog: String = ""
            var blogs: List<String> = listOf()
            lateinit var blogsArray: ArrayList<String>
            if (viewModel.userInfo != null) {
                primaryBlog = viewModel.userInfo.primary_blog
                blogs = viewModel.userInfo.blogs
                blogsArray = ArrayList(blogs)
            } else {
                Toast.makeText(this, "Could not retrieve user info", Toast.LENGTH_SHORT).show()
            }
            Intent(this, AddPostActivity::class.java).also {
                it.putExtra("EXTRA_PRIMARY_BLOG", primaryBlog)
                it.putExtra("EXTRA_BLOGS", blogsArray)
                startActivity(it)
            }
        }

        lifecycleScope.launch {
            val token = sharedPref.getString("token", null)
            if (token != null) {
                val response = viewModel.getUserInfo(token)
                if (response.isSuccessful) {
                    Toast.makeText(this@HomePageActivity, "User Info Retrieved", Toast.LENGTH_SHORT)
                        .show()
                    viewModel.userInfo = response.body()!!
                } else {
                    Toast.makeText(
                        this@HomePageActivity,
                        "Oops, something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        lifecycleScope.launch {
            val token = sharedPref.getString("token", null)
            if (token != null) {
                val postsResponse = viewModel.getDashboardPosts(token)
                if (postsResponse.isSuccessful) {
                    binding.rvDashboard.apply{
                        myAdapter = DashboardAdapter(postsResponse.body()!!.forYouPosts)
                        layoutManager = manager
                        adapter = myAdapter
                    }
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
                R.id.ic_logout -> {
                    val editor = sharedPref.edit()
                    editor.clear()
                    editor.apply()
                    startActivity(
                        Intent(
                            this, IntroActivity::class.java
                        )
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    return@setOnItemSelectedListener false
                }
                else ->{ return@setOnItemSelectedListener false }
            }
        }

        // init the adapter
//        adapter = PostAdapter(this, 0, postList)
//
//        // init the listView
//        binding.postList.adapter = adapter
//
//        // adding some posts
//        postList.add(PostItem("text 1", null, "https://stackoverflow.com", "User 1"))
//        postList.add(PostItem("text 2", null, "https://stackoverflow.com", "User 2"))
//        postList.add(PostItem("text 3", null, "https://stackoverflow.com", "User 3"))
//        postList.add(PostItem("text 4", null, "https://stackoverflow.com", "User 4"))
//        postList.add(PostItem("text 5", null, "https://stackoverflow.com", "User 5"))
//        postList.add(PostItem("text 6", null, "https://stackoverflow.com", "User 6"))
//        postList.add(PostItem("text 7", null, "https://stackoverflow.com", "User 7"))
//        // notify the adapter
//        adapter.notifyDataSetChanged()
    }


}