package com.example.sns.adapter

import android.annotation.SuppressLint
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sns.R
import com.example.sns.databinding.CommentItemBinding
import com.example.sns.network.model.Comment
import com.example.sns.utils.BASE_URL
import com.example.sns.utils.DateTimeConverter
import kotlinx.android.synthetic.main.comment_item.view.*

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.CommentHolder>() {

    var commentList = ArrayList<Comment>()
    var selectedItem = SparseBooleanArray(0)

    fun setCommentLIst(commentList : ArrayList<Comment>) {
        this.commentList.clear()
        this.commentList.addAll(commentList.also {
            it.forEach { data ->
                data.created_at = DateTimeConverter.jsonTimeToTime(data.created_at)
            }
        })
        this.commentList.reverse()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CommentHolder(DataBindingUtil.inflate(
        LayoutInflater.from(parent.context), R.layout.comment_item, parent, false))

    override fun getItemCount() = commentList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CommentHolder, position: Int) {

        if (onLongClickListener != null) {
            holder.holder.setOnLongClickListener { v ->
                onLongClickListener?.onClick(v, position, holder)!!
            }
        }

        if (onEditReplyClickListener != null) {
            holder.editReply.setOnClickListener { v ->
                onEditReplyClickListener?.onClick(v, position, holder)
            }
        }

        if (onShowReplyClickListener != null) {
            holder.showReply.setOnClickListener { v ->
                onShowReplyClickListener?.onClick(v, position, holder)
            }
        }

        if (selectedItem.get(position, false)) {
            holder.showReply.visibility = View.VISIBLE
            holder.recyclerView.visibility = View.VISIBLE
            holder.showReply.text = "이전 답글 보기 (${commentList[position].reply}개)"
        } else {
            if (commentList[position].reply > 0) {
                holder.showReply.visibility = View.VISIBLE
                holder.showReply.text = "답글 보기 (${commentList[position].reply}개)"
            } else {
                holder.showReply.visibility = View.GONE
            }
            holder.recyclerView.visibility = View.GONE
        }

        holder.recyclerView.setHasFixedSize(true)
        holder.recyclerView.adapter = holder.replyAdapter

        val item = commentList[position]
        holder.bind(item)
    }

    inner class CommentHolder(private val binding: CommentItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val recyclerView : RecyclerView = binding.recyclerViewReply
        val holder: ConstraintLayout = binding.layoutHolder
        val editReply : TextView = binding.editReply
        val replyHolder : ConstraintLayout = binding.replyHolder
        val showReply : TextView = binding.showMoreReply
        val replyAdapter = ReplyAdapter()

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

    fun clearSelectedItem() {
        var position: Int
        for (index: Int in 0 until selectedItem.size()) {
            position = selectedItem.keyAt(index)
            selectedItem.put(position, false)
        }
    }

    var onShowReplyClickListener : OnItemClickListener? = null
    var onLongClickListener: OnItemLongClickListener? = null
    var onEditReplyClickListener : OnItemClickListener? = null

    interface OnItemLongClickListener {
        fun onClick(
            view: View,
            position: Int,
            holder: CommentHolder
        ): Boolean
    }

    interface OnItemClickListener {
        fun onClick(
            view: View,
            position: Int,
            holder: CommentHolder
        )
    }
}