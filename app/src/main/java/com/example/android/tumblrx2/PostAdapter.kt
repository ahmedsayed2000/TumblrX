package com.example.android.tumblrx2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.android.tumblrx2.responses.dashboardpost.Post
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.views.GPHMediaView
import com.giphy.sdk.ui.views.GifView
import com.squareup.picasso.Picasso
import io.github.ponnamkarthik.richlinkpreview.RichLinkView
import io.github.ponnamkarthik.richlinkpreview.ViewListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.droidsonroids.gif.GifImageView
import java.lang.Exception

class PostAdapter : ArrayAdapter<Post> {

    var listOfPosts: MutableList<Post>

    constructor(context: Context, res: Int, postList: MutableList<Post>) : super(
        context,
        res,
        postList
    ) {
        listOfPosts = postList
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view = convertView

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false)
        }

        // the post element
        val item = listOfPosts.get(position)

        // setting the blog name
        val blogName = view?.findViewById<TextView>(R.id.blog_name)
        blogName?.setText(item.blogAttribution.handle)

        // setting the blog image


        // setting the view elements (image , text , link , gif)
        val text = view?.findViewById<TextView>(R.id.post_text)
        val image = view?.findViewById<ImageView>(R.id.post_image)
        val link = view?.findViewById<RichLinkView>(R.id.post_link)
        val gif = view?.findViewById<GifImageView>(R.id.post_gif)

        var postImage: String = ""
        var postText: String = ""
        var postLink: String = ""
        var postGif: String = ""

        for (content in item.content) {
            if (content != null) {
                if (content.type == "text") {
                    postText = content.text
                } else if (content.type == "image") {
                    postImage = content.url
                } else if (content.type == "link") {
                    postLink = content.url
                } else if(content.type =="image/gif"){
                    postGif = content.url
                }
            }
        }
        if (postText != "") {
            text?.setText(postText)
        } else {
            text?.visibility = View.GONE
        }

        if (postImage != "") {
//            Glide.with(convertView!!.context).load(postImage).centerCrop().into(image!!)
                Picasso.get().load(postImage).into(image)

        } else {
            image?.visibility = View.GONE
        }

        if (postLink != "") {
            link?.setLink(postLink, object : ViewListener {
                override fun onSuccess(status: Boolean) {
//                    Toast.makeText(context, "Link added", Toast.LENGTH_SHORT).show()
                }

                override fun onError(e: Exception?) {
                }
            })
        } else{
            link?.visibility = View.GONE
        }

        if(postGif != ""){
            Picasso.get().load(postGif).into(gif)
        }else {
            gif?.visibility=View.GONE
        }
        return view!!

    }
}