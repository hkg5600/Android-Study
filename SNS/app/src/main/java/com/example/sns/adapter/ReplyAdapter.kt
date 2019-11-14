package com.example.sns.adapter

import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
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
import com.example.sns.databinding.ReplyItemBinding
import com.example.sns.network.model.ProfileImage
import com.example.sns.network.model.Reply
import com.example.sns.utils.BASE_URL
import com.example.sns.utils.DateTimeConverter
import com.example.sns.utils.UserObject
import kotlinx.android.synthetic.main.comment_item.view.owner_profile_image

class ReplyAdapter : RecyclerView.Adapter<ReplyAdapter.ReplyHolder>() {

    data class SpanReply(
        val id: Int,
        val profile_image: ProfileImage,
        val text: SpannableString,
        var created_at: String,
        val comment: Int,
        val owner: String,
        val like: ArrayList<String>
    )

    var replyList = ArrayList<SpanReply>()

    fun setReplyLIst(replyLIst: ArrayList<Reply>) {
        this.replyList.clear()
        this.replyList.addAll(with(replyLIst) {
            ArrayList(this.map { SpanReply(it.id, it.profile_image, getSpannable(it.text, getClickableSpan(it.owner), 0, it.owner.length), DateTimeConverter.jsonTimeToTime(it.created_at), it.comment, it.owner, it.like) }) }
        )
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReplyHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.reply_item,
            parent,
            false
        )
    )

    override fun getItemCount() = replyList.size

    override fun onBindViewHolder(holder: ReplyHolder, position: Int) {

        if (replyList[position].like.contains(UserObject.userInfo?.user?.user_id)) {
            holder.likeButton.setImageResource(R.drawable.ic_like)
        } else {
            holder.likeButton.setImageResource(R.drawable.ic_unlike)
        }

        holder.likeCount.text = "좋아요 ${replyList[position].like.size}개"

        if (onLikeButtonClickListener != null) {
            holder.likeButton.setOnClickListener { v ->
                onLikeButtonClickListener?.onClick(v, position, holder)
            }
        }

        holder.textReply.movementMethod = LinkMovementMethod.getInstance()
        holder.bind(replyList[position])
    }

    inner class ReplyHolder(private val binding: ReplyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val layoutHolder: ConstraintLayout = binding.layoutHolder
        val likeButton: ImageButton = binding.likeReply
        val likeCount: TextView = binding.likeCountReply
        val textReply: TextView = binding.textViewReply

        fun bind(item: SpanReply) {

            if (item.profile_image.profile_image != null) {
                itemView.run {
                    Glide.with(context).load(BASE_URL + item.profile_image.profile_image)
                        .apply(RequestOptions.circleCropTransform()).into(owner_profile_image)
                }
            }
            binding.item = item
        }
    }

    var onLikeButtonClickListener: OnItemClickListener? = null
    var onShowReplyClickListener: OnItemClickListener? = null
    var onLongClickListener: OnItemLongClickListener? = null
    var onEditReplyClickListener: OnItemClickListener? = null
    var onOwnerTextClickListener : OnNameClickListener? = null

    interface OnItemLongClickListener {
        fun onClick(
            view: View,
            position: Int,
            holder: ReplyHolder
        ): Boolean
    }

    interface OnItemClickListener {
        fun onClick(
            view: View,
            position: Int,
            holder: ReplyHolder
        )
    }

    interface OnNameClickListener {
        fun onClick(name: String)
    }

    private fun getClickableSpan(name: String) = object : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
            ds.color = Color.DKGRAY
            ds.isUnderlineText = false
        }

        override fun onClick(p0: View) {
            if (onOwnerTextClickListener != null) {
                onOwnerTextClickListener?.onClick(name)
            }
        }
    }

    private fun getSpannable(complete: String, clickableSpan: ClickableSpan, start: Int, end: Int
    ): SpannableString {
        lateinit var replyText: SpannableString
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            replyText =
                SpannableString(Html.fromHtml(complete, Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE))
        } else {
            @Suppress("DEPRECATION")
            replyText = SpannableString(Html.fromHtml(complete))
        }
        replyText.setSpan(clickableSpan, start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        return replyText
    }
}