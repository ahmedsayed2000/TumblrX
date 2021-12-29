package com.example.android.tumblrx2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.views.GPHMediaView
import io.github.ponnamkarthik.richlinkpreview.RichLinkView
import io.github.ponnamkarthik.richlinkpreview.ViewListener
import java.lang.Exception

class PostAdapter: ArrayAdapter<PostItem> {

    var listOfPosts: MutableList<PostItem>

    constructor(context: Context, res: Int,  postList: MutableList<PostItem>) : super(context, res, postList) {
        listOfPosts = postList
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {



        var view = convertView

        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false)
        }

        // the post element
        val item = listOfPosts.get(position)

        // setting the blog name
        val blogName = view?.findViewById<TextView>(R.id.blog_name)
        blogName?.setText(item.blogName)

        // setting the blog image


        // setting the view elements (image , text , link , gif)
        val text = view?.findViewById<TextView>(R.id.post_text)
        val image = view?.findViewById<ImageView>(R.id.post_image)
        val link = view?.findViewById<RichLinkView>(R.id.post_link)
        //val gif = view.findViewById<GPHMediaView>(R.id.post_gif)

        if(item.text != null) {
            text?.setText(item.text)
        }
        else{
            text?.visibility = View.GONE
        }

        if(item.image != null) {

        }
        else{
            image?.visibility = View.GONE
        }

        if(item.link != null) {
            link?.setLink(item.link, object: ViewListener{
                override fun onSuccess(status: Boolean) {

                }

                override fun onError(e: Exception?) {
                }
            })
        }
        else{
            link?.visibility = View.GONE
        }





        return view!!
    }
}