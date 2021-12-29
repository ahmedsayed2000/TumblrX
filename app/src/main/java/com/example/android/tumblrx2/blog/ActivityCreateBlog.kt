package com.example.android.tumblrx2.blog

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.android.tumblrx2.R
import com.example.android.tumblrx2.login.BlogModelView
import com.example.android.tumblrx2.login.LoginViewModel
import com.example.android.tumblrx2.responses.CreateBlogResponse
import com.example.android.tumblrx2.responses.LoginResponse
import retrofit2.HttpException
import java.io.IOException

class ActivityCreateBlog : AppCompatActivity() {
    private lateinit var viewModel: BlogModelView
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = this.getSharedPreferences("appPref", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TumblrX2)
        setContentView(R.layout.activity_create_blog)
        viewModel = ViewModelProvider(this)[BlogModelView::class.java]

        findViewById<Button>(R.id.create_blog_btn).setOnClickListener {
            val title=findViewById<EditText>(R.id.editTextTextPersonName3).text.trim().toString()
            val code = viewModel.validateInput(title)
            if (code != 0) {
                Toast.makeText(getApplicationContext(),
                    viewModel.chooseErrMsg(code),
                    Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),
                    title,
                    Toast.LENGTH_SHORT).show();
                lifecycleScope.launchWhenCreated {
                    val token = sharedPref.getString("token", null)
                    val blogId = sharedPref.getString("primaryBlogId", null)
                    val response: retrofit2.Response<CreateBlogResponse> = try {
                        viewModel.createBlog(title,blogId!!,token!!)
                    } catch (e: IOException) {
                        Toast.makeText(getApplicationContext(),
                            "You might not have internet connection",
                            Toast.LENGTH_SHORT).show();
                        return@launchWhenCreated
                    } catch (e: HttpException) {
                        Toast.makeText(getApplicationContext(),
                            e.toString(),
                            Toast.LENGTH_SHORT).show();
                        return@launchWhenCreated
                    }
                    if (response.isSuccessful) {
                        Toast.makeText(getApplicationContext(),
                            "Created",
                            Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),
                            "Error In Creating",
                            Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }

    }
}