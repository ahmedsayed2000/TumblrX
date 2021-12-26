package com.example.android.tumblrx2.blog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.android.tumblrx2.R
import com.example.android.tumblrx2.login.BlogModelView
import com.example.android.tumblrx2.login.LoginViewModel

class ActivityCreateBlog : AppCompatActivity() {
    private lateinit var viewModel: BlogModelView
    override fun onCreate(savedInstanceState: Bundle?) {
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
            }

        }

    }
}