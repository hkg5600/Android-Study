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
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sns.R
import com.example.sns.databinding.PostItemBinding
import com.example.sns.network.model.Post
import com.example.sns.network.model.PostImage
import com.example.sns.network.model.ProfileImage
import com.example.sns.utils.BASE_URL
import com.example.sns.utils.DateTimeConverter
import com.example.sns.utils.SingleLiveEvent
import kotlinx.android.synthetic.main.post_item.view.*
import kotlin.collections.ArrayList


class PostAdapter : RecyclerView.Adapter<PostAdapter.PostHolder>() {
    data class SpanPost(
        val id: Int,
        val text: SpannableString,
        val owner: String,
        var created_at: String,
        val images : ArrayList<PostImage>,
        val like : ArrayList<String>,
        val profile_image: ProfileImage
    )
    var clickUserNameText : SingleLiveEvent<String> = SingleLiveEvent()
    var postList = ObservableArrayList<SpanPost>()
    var userName = ""
    var selectedItem = SparseBooleanArray(0)
    var nextPage = 0
    var lastPage = false
    var loadMore: SingleLiveEvent<Int> = SingleLiveEvent()

    fun setPost(postList: ArrayList<Post>) {
        this.postList.addAll(with(postList) {
            ArrayList(this.map { SpanPost(it.id, getSpannable(it.text, getClickableSpan(it.owner), 0, it.owner.length), it.owner, DateTimeConverter.jsonTimeToTime(it.created_at), it.images, it.like, it.profile_image) }) }
        )

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PostHolder, position: Int) {

        if (position > postList.size - 2 && !lastPage) {
            loadMore.call()
        }

        postList[position].like.run {
            if (isEmpty()) holder.likeCount.text = "첫 좋아요를 눌러주세요"
            else holder.likeCount.text = "${this[0]}님 외 ${this.size-1}명이 좋아합니다"
        }

        if (onShowLikeClickListener!= null) {
            holder.likeCount.setOnClickListener { v ->
                onShowLikeClickListener?.onClick(v, position, holder)
            }
        }

        if (onCommentBtnClickListener != null) {
            holder.editComment.setOnClickListener { v ->
                onCommentBtnClickListener?.onClick(v, position, holder)
            }
        }

        if (onItemClickListener != null) {
            holder.btnPostOptions.setOnClickListener { v ->
                onItemClickListener?.onClick(v, position, holder)
            }
        }

        if (onLikeClickListener != null) {
            holder.btnLike.setOnClickListener { v ->
                onLikeClickListener?.onClick(v, position, holder)
            }
        }

        if (onShowDetailClickListener != null) {
            holder.showPostDetail.setOnClickListener { v ->
                onShowDetailClickListener?.onClick(v, position, holder)
            }
        }

        if (postList[position].like.contains(userName)) {
            holder.btnLike.setImageResource(R.drawable.ic_like)
        } else {
            holder.btnLike.setImageResource(R.drawable.ic_unlike)
        }

        if (selectedItem.get(position, false)) {
            val params = holder.textViewText.layoutParams.apply {
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                width = 0
            }
            holder.textViewText.layoutParams = params
            holder.btnShowFullText.visibility = View.GONE
        } else {
            val params = holder.textViewText.layoutParams.apply {
                height = 60
                width = 0
            }
            holder.textViewText.layoutParams = params
        }

        if (postList[position].text.length < 20 || selectedItem.get(position, false)) {
            holder.showDetail = true
            holder.btnShowFullText.visibility = View.GONE
        } else {
            holder.btnShowFullText.visibility = View.VISIBLE
        }

        holder.textViewText.movementMethod = LinkMovementMethod.getInstance()

        val item = postList[position]
        holder.bind(item)
    }

    class PostHolder(private val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val btnShowFullText: TextView = binding.textViewShow
        val textViewText: TextView = binding.textViewText
        private val viewPager: ViewPager2 = binding.viewPager
        val btnPostOptions: ImageButton = binding.buttonOptions
        val btnLike: ImageButton = binding.ImageLike
        val showPostDetail: ConstraintLayout = binding.toDetail
        val editComment: ImageButton = binding.showComment
        val likeCount: TextView = binding.likeCount

        var showDetail = false

        fun bind(item: SpanPost) {
            binding.imgViewHolder.visibility = View.GONE
            if (item.images.isNotEmpty()) {
                binding.imgViewHolder.visibility = View.VISIBLE
                viewPager.adapter = PostImageListAdapter(item.images)
                viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            }
            if (item.profile_image.profile_image != null) {
                itemView.run {
                    Glide.with(context).load(BASE_URL + item.profile_image.profile_image)
                        .apply(RequestOptions.circleCropTransform()).into(img_profile)
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

    var onShowLikeClickListener : OnItemClickListener? = null
    var onCommentBtnClickListener: OnItemClickListener? = null
    var onItemClickListener: OnItemClickListener? = null
    var onShowDetailClickListener: OnItemClickListener? = null
    var onLikeClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClick(
            view: View,
            position: Int,
            holder: PostHolder
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