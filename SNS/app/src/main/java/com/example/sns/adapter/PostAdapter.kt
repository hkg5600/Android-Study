package com.example.sns.adapter

import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sns.R
import com.example.sns.databinding.PostItemBinding
import com.example.sns.network.model.Post
import com.example.sns.utils.BASE_URL
import com.example.sns.utils.DateTimeConverter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.post_item.view.*
import org.w3c.dom.Text


class PostAdapter : RecyclerView.Adapter<PostAdapter.PostHolder>() {

    var postList = ObservableArrayList<Post>()

    var selectedItem = SparseBooleanArray(0)

    fun setPost(postList: ArrayList<Post>) {
        this.postList.clear()
        postList.forEach {
            val data = Post(it.id, it.text, it.owner, DateTimeConverter.jsonTimeToTime(it.created_at), if (it.image != null) BASE_URL + it.image else null)
            this.postList.add(data)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder =
        PostHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.post_item,
                parent,
                false
            )
        )

    override fun getItemCount() = postList.size

    override fun onBindViewHolder(holder: PostHolder, position: Int) {

        holder.btnShow.setOnClickListener {
            clearSelectedItem()
            selectedItem.put(position, true)
            val params = holder.textView.layoutParams.apply {
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                width = 850
            }
            holder.textView.layoutParams = params
            holder.btnShow.visibility = View.GONE
            notifyDataSetChanged()
        }


        if (selectedItem.get(position, false)) {
            val params = holder.textView.layoutParams.apply {
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                width = 850
            }
            holder.textView.layoutParams = params
            holder.btnShow.visibility = View.GONE
        } else {
            val params = holder.textView.layoutParams.apply {
                height = 50
                width = 150
            }
            holder.textView.layoutParams = params
        }

        if (postList[position].text.length < 20 || selectedItem.get(position, false)) {
            holder.btnShow.visibility = View.GONE
        } else {
            holder.btnShow.visibility = View.VISIBLE
        }

        val item = postList[position]
        holder.bind(item)
    }

    class PostHolder(private val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val btnShow: TextView = binding.textViewShow
        val textView: TextView = binding.textViewText

        fun bind(item: Post) {
            binding.item = item
            if (item.image != null) {
                itemView.run {
                    Glide.with(context).load(item.image).into(img_view)
                }
            } else {
                binding.imgView.visibility = View.GONE
            }

        }
    }

    fun clearSelectedItem() {
        var position: Int
        for (index: Int in 0 until selectedItem.size()) {
            position = selectedItem.keyAt(index)
            selectedItem.put(position, false)
        }
    }

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClick(
            view: View,
            position: Int,
            holder: PostHolder
        )
    }
}