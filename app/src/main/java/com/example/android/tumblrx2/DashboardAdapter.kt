package com.example.android.tumblrx2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.tumblrx2.responses.dashboarddata.ForYouPost

class DashboardAdapter(private val data: List<ForYouPost>) :
    RecyclerView.Adapter<DashboardAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(post: ForYouPost) {
            val blogName = view.findViewById<TextView>(R.id.blog_name)
            val postText = view.findViewById<TextView>(R.id.post_text)
            val postImage = view.findViewById<ImageView>(R.id.post_image)

            blogName.text = post.blogAttribution.title
            postText.text = post.content[0].text

            if (post.content[0].type == "image") {
                val imageUrl = post.content[0].url
                Glide.with(view.context).load(imageUrl).centerCrop().into(postImage)
            } else {
                postImage.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data[position])
    }


}
