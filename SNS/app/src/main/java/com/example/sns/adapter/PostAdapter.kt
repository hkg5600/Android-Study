package com.example.sns.adapter

import android.content.Context
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sns.R
import com.example.sns.network.model.Post
import kotlinx.android.synthetic.main.post_item.view.*

class PostAdapter : RecyclerView.Adapter<PostAdapter.PostHolder>() {

    private var postList = ArrayList<Post>()

    fun setPost(postList: ArrayList<Post>) {
        Log.d("Msg", "In setPost")

        this.postList = postList
        Log.d("Data", "${postList[0].title}")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostHolder(parent)

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        postList[position].let { data ->
            with(holder) {
                title.text = data.title
                text.text = data.text
                create_at.text = data.created_at.toString()
                owner.text = data.owner
            }
        }
    }

    class PostHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
    ) {
        val title = itemView.text_view_title
        val text = itemView.text_view_text
        val owner = itemView.text_view_owner
        val create_at = itemView.text_view_create_time
    }
}