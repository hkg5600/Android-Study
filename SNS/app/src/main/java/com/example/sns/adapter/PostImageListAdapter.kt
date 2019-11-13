package com.example.sns.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sns.R
import com.example.sns.databinding.PostImageItemBinding
import com.example.sns.network.model.PostImage
import com.example.sns.utils.BASE_URL
import kotlinx.android.synthetic.main.post_image_item.view.*

class PostImageListAdapter(private var imageList: ArrayList<PostImage>) :
    RecyclerView.Adapter<PostImageListAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.post_image_item, parent, false
        )
    )

    override fun getItemCount() = imageList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.textView.text = "${imageList.indexOf(imageList[position]) + 1}/${imageList.size}"
        holder.bind(imageList[position])
    }

    inner class PostViewHolder(private val binding: PostImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val textView: TextView = binding.textViewPosition

        fun bind(item: PostImage) {

            itemView.run {
                Glide.with(context).load(BASE_URL + item.image).into(img_view)
            }

        }
    }

}