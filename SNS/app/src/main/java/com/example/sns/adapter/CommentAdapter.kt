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
import com.example.sns.utils.UserObject
import kotlinx.android.synthetic.main.comment_item.view.*



class CommentAdapter : RecyclerView.Adapter<CommentAdapter.CommentHolder>() {

    data class SpanComment(
        val id: Int,
        val text: SpannableString,
        var reply_count: Int,
        val like: ArrayList<String>,
        var created_at: String,
        val owner: String,
        val post: Int,
        val profile_image: ProfileImage
    )

    var clickUserNameText : SingleLiveEvent<String> = SingleLiveEvent()
    var commentList = ArrayList<SpanComment>()
    var selectedItem = SparseBooleanArray(0)
    var nextPage = 0
    var lastPage = false
    var loadMore: SingleLiveEvent<Int> = SingleLiveEvent()
    var likeToReply : SingleLiveEvent<Int> = SingleLiveEvent()
    var unLikeToReply : SingleLiveEvent<Int> = SingleLiveEvent()
    var deleteReply : SingleLiveEvent<Int> = SingleLiveEvent()
    var reportReply : SingleLiveEvent<Int> = SingleLiveEvent()

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
    override fun onBindViewHolder(commentHolder: CommentHolder, position: Int) {

        if (position > commentList.size - 2 && !lastPage) {
            loadMore.call()
        }

        if (onLikeButtonClickListener != null) {
            commentHolder.likeComment.setOnClickListener { v ->
                onLikeButtonClickListener?.onClick(v, position, commentHolder)
            }
        }

        if (onLongClickListener != null) {
            commentHolder.holder.setOnLongClickListener { v ->
                onLongClickListener?.onClick(v, position, commentHolder)!!
            }
        }

        if (onEditReplyClickListener != null) {
            commentHolder.editReply.setOnClickListener { v ->
                onEditReplyClickListener?.onClick(v, position, commentHolder)
            }
        }

        if (onShowReplyClickListener != null) {
            commentHolder.showReply.setOnClickListener { v ->
                onShowReplyClickListener?.onClick(v, position, commentHolder)
            }
        }

        if (commentList[position].like.contains(UserObject.userInfo?.user?.user_id)) {
            commentHolder.likeComment.setImageResource(R.drawable.ic_like)
        } else {
            commentHolder.likeComment.setImageResource(R.drawable.ic_unlike)
        }

        commentHolder.likeCount.text = "좋아요 ${commentList[position].like.size}개"

        if (commentList[position].reply_count > 0) {
            commentHolder.showReply.text = "답글 보기 (${commentList[position].reply_count}개)"
            commentHolder.showReply.visibility = View.VISIBLE
        } else
            commentHolder.showReply.visibility = View.GONE

        if (selectedItem.get(position, false)) {
            commentHolder.recyclerView.visibility = View.VISIBLE
                commentHolder.showReply.text = "이전 답글 보기 (${commentHolder.nextCount}개)"
        } else {
            commentHolder.recyclerView.visibility = View.GONE
        }

        commentHolder.textViewComment.movementMethod = LinkMovementMethod.getInstance()
        commentHolder.recyclerView.setHasFixedSize(true)
        commentHolder.recyclerView.adapter = commentHolder.replyAdapter

        commentHolder.replyAdapter.onLikeButtonClickListener = object : ReplyAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int, holder: ReplyAdapter.ReplyHolder) {
                commentHolder.replyAdapter.replyList[position].like.run {
                    if (contains(UserObject.userInfo?.user?.user_id)) {
                        holder.likeButton.setImageResource(R.drawable.ic_unlike)
                        remove(UserObject.userInfo?.user?.user_id!!)
                        unLikeToReply.value = commentHolder.replyAdapter.replyList[position].id
                    } else {
                        holder.likeButton.setImageResource(R.drawable.ic_like)
                        add(UserObject.userInfo?.user?.user_id!!)
                        likeToReply.value = commentHolder.replyAdapter.replyList[position].id
                    }
                    holder.likeCount.text = "좋아요 ${commentHolder.replyAdapter.replyList[position].like.size}개"
                }
            }
        }

        commentHolder.replyAdapter.onLongClickListener = object : ReplyAdapter.OnItemLongClickListener {
            override fun onClick(view: View, position: Int, holder: ReplyAdapter.ReplyHolder): Boolean {
                if (commentHolder.replyAdapter.replyList[position].owner == UserObject.userInfo?.user?.user_id)
                    deleteReply.value = commentHolder.replyAdapter.replyList[position].id
                else
                    reportReply.value = commentHolder.replyAdapter.replyList[position].id
                return true
            }

        }

        commentHolder.replyAdapter.onOwnerTextClickListener = object : ReplyAdapter.OnNameClickListener {
            override fun onClick(name: String) {
                clickUserNameText.value = name
            }
        }

        val item = commentList[position]
        commentHolder.bind(item)
    }

    inner class CommentHolder(private val binding: CommentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var nextReply = 0
        var nextCount = 0
        val textViewComment : TextView = binding.textViewComment
        val recyclerView: RecyclerView = binding.recyclerViewReply
        val holder: ConstraintLayout = binding.layoutHolder
        val editReply: TextView = binding.editReply
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
        lateinit var commentText: SpannableString
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            commentText =
                SpannableString(Html.fromHtml(complete, Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE))
        } else {
            @Suppress("DEPRECATION")
            commentText = SpannableString(Html.fromHtml(complete))
        }
        commentText.setSpan(clickableSpan, start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        return commentText
    }
}