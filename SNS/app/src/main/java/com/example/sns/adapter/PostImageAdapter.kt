package com.example.sns.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sns.R
import com.example.sns.databinding.PostImageItemBinding
import com.example.sns.network.model.PostImage
import com.example.sns.utils.BASE_URL
import kotlinx.android.synthetic.main.post_image_item.view.*

class PostImageAdapter( private var imageList : ArrayList<PostImage>) : RecyclerView.Adapter<PostImageAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.post_image_item, parent, false
        )
    )

    override fun getItemCount() = imageList.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(imageList[position].image)
    }

    inner class PostViewHolder(private val binding: PostImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            itemView.run {
                Glide.with(context).load(BASE_URL + item).into(img_view)
            }

        }
    }

}