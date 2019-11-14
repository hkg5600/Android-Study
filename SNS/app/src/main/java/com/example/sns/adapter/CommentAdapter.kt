package com.example.sns.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sns.R
import com.example.sns.databinding.CommentItemBinding
import com.example.sns.network.model.Comment
import com.example.sns.network.model.ProfileImage
import com.example.sns.utils.BASE_URL
import com.example.sns.utils.DateTimeConverter
import com.example.sns.utils.SingleLiveEvent
import kotlinx.android.synthetic.main.comment_item.view.*

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.CommentHolder>() {

    data class SpanComment(
        val id: Int,
        val text: SpannableString,
        val reply_count: Int,
        val like: ArrayList<String>,
        var created_at: String,
        val owner: String,
        val post: Int,
        val profile_image: ProfileImage
    )
    var clickUserNameText : SingleLiveEvent<String> = SingleLiveEvent()
    var userName = ""
    var commentList = ArrayList<SpanComment>()
    var selectedItem = SparseBooleanArray(0)
    var nextPage = 0
    var lastPage = false
    var loadMore: SingleLiveEvent<Int> = SingleLiveEvent()

    fun setCommentLIst(commentList: ArrayList<Comment>) {
        this.commentList.addAll(with(commentList) {
            ArrayList(this.map {
                SpanComment(it.id, getSpannable(it.text, getClickableSpan(it.owner), 0, it.owner.length), it.reply_count, it.like, DateTimeConverter.jsonTimeToTime(it.created_at), it.owner, it.post, it.profile_image)
            }) }
        )
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CommentHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.comment_item, parent, false
        )
    )

    override fun getItemCount() = commentList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CommentHolder, position: Int) {

        if (position > commentList.size - 2 && !lastPage) {
            loadMore.call()
        }

        if (onLikeButtonClickListener != null) {
            holder.likeComment.setOnClickListener { v ->
                onLikeButtonClickListener?.onClick(v, position, holder)
            }
        }

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

        if (commentList[position].like.contains(userName)) {
            holder.likeComment.setImageResource(R.drawable.ic_like)
        } else {
            holder.likeComment.setImageResource(R.drawable.ic_unlike)
        }

        holder.likeCount.text = "좋아요 ${commentList[position].like.size}개"

        if (commentList[position].reply_count > 0) {
            holder.showReply.text = "답글 보기 (${commentList[position].reply_count}개)"
            holder.showReply.visibility = View.VISIBLE
        } else
            holder.showReply.visibility = View.GONE

        if (selectedItem.get(position, false)) {
            holder.recyclerView.visibility = View.VISIBLE
            holder.showReply.text = "이전 답글 보기 (${commentList[position].reply_count}개)"
        } else {
            holder.recyclerView.visibility = View.GONE
        }
        holder.textViewComment.movementMethod = LinkMovementMethod.getInstance()
        holder.recyclerView.setHasFixedSize(true)
        holder.recyclerView.adapter = holder.replyAdapter

        val item = commentList[position]
        holder.bind(item)
    }

    inner class CommentHolder(private val binding: CommentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val textViewComment : TextView = binding.textViewComment
        val recyclerView: RecyclerView = binding.recyclerViewReply
        val holder: ConstraintLayout = binding.layoutHolder
        val editReply: TextView = binding.editReply
        val replyHolder: ConstraintLayout = binding.replyHolder
        val showReply: TextView = binding.showMoreReply
        val likeCount: TextView = binding.likeCount
        val likeComment: ImageButton = binding.likeComment
        val replyAdapter = ReplyAdapter()

        fun bind(item: SpanComment) {
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
        selectedItem.clear()
        notifyDataSetChanged()
    }

    var onLikeButtonClickListener: OnItemClickListener? = null
    var onShowReplyClickListener: OnItemClickListener? = null
    var onLongClickListener: OnItemLongClickListener? = null
    var onEditReplyClickListener: OnItemClickListener? = null

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

    private fun getClickableSpan(name: String) = object : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
            ds.color = Color.DKGRAY
            ds.isUnderlineText = false
        }

        override fun onClick(p0: View) {
            clickUserNameText.value = name
        }
    }

    private fun getSpannable(complete: String, clickableSpan: ClickableSpan, start: Int, end: Int): SpannableString {
        lateinit var postText: SpannableString
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            postText =
                SpannableString(Html.fromHtml(complete, Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE))
        } else {
            @Suppress("DEPRECATION")
            postText = SpannableString(Html.fromHtml(complete))
        }
        postText.setSpan(clickableSpan, start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        return postText
    }
}