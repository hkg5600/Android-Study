package com.example.sns.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sns.R
import com.example.sns.databinding.ActivityPostDetailBinding
import com.example.sns.databinding.CommentItemBinding
import com.example.sns.network.model.Comment
import com.example.sns.utils.BASE_URL
import com.example.sns.utils.DateTimeConverter
import kotlinx.android.synthetic.main.comment_item.view.*
import kotlinx.android.synthetic.main.post_item.view.*

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.CommentHolder>() {

    var commentList = ArrayList<Comment>()

    fun setCommentLIst(commentList : ArrayList<Comment>) {
        this.commentList.clear()
        commentList.forEach {
            val data = Comment(it.id, it.text, it.like, DateTimeConverter.jsonTimeToTime(it.created_at), it.owner, it.post,it.profile_image)
            this.commentList.add(data)
        }
        this.commentList.reverse()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CommentHolder(DataBindingUtil.inflate(
        LayoutInflater.from(parent.context), R.layout.comment_item, parent, false))

    override fun getItemCount() = commentList.size

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {

        if (onLongClickListener != null) {
            holder.holder.setOnLongClickListener { v ->
                onLongClickListener?.onClick(v, position, holder)!!
            }
        }

        val item = commentList[position]
        holder.bind(item)
    }

    inner class CommentHolder(private val binding: CommentItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val holder: ConstraintLayout = binding.layoutHolder

        fun bind(item: Comment) {
            if (item.profile_image.profile_image != null) {
                itemView.run {
                    Glide.with(context).load(BASE_URL + item.profile_image.profile_image)
                        .apply(RequestOptions.circleCropTransform()).into(owner_profile_image)
                }
            }
            binding.item = item
        }
    }

    var onLongClickListener: OnItemLongClickListener? = null

    interface OnItemLongClickListener {
        fun onClick(
            view: View,
            position: Int,
            holder: CommentHolder
        ): Boolean
    }
}