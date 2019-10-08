package com.example.sns.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.example.sns.R
import com.example.sns.databinding.PostItemBinding
import com.example.sns.network.model.Post


class PostAdapter : RecyclerView.Adapter<PostAdapter.PostHolder>() {

    var postList = ObservableArrayList<Post>()

    fun setPost(postList: ObservableArrayList<Post>) {
        Log.d("Msg", "In setPost")
        this.postList = postList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : PostHolder =
        PostHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.post_item, parent, false))

    override fun getItemCount() = postList.size

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val item = postList[position]
        holder.bind(item)
    }

    class PostHolder(private val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root)
     {
        fun bind(item: Post) {
            binding.item = item
        }
    }
}