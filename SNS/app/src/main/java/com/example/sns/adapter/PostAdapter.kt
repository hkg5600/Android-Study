package com.example.sns.adapter

import android.annotation.SuppressLint
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
import com.example.sns.utils.BASE_URL
import com.example.sns.utils.DateTimeConverter
import com.example.sns.utils.SingleLiveEvent
import kotlinx.android.synthetic.main.post_item.view.*
import java.util.*
import kotlin.collections.ArrayList


class PostAdapter : RecyclerView.Adapter<PostAdapter.PostHolder>() {

    var postList = ObservableArrayList<Post>()
    var userName = ""
    var selectedItem = SparseBooleanArray(0)
    var nextPage = 0
    var lastPage = false
    var loadMore: SingleLiveEvent<Int> = SingleLiveEvent()

    fun setPost(postList: ArrayList<Post>) {
        this.postList.addAll(postList.also {
            it.forEach {data ->
                data.created_at = DateTimeConverter.jsonTimeToTime(data.created_at)
            }
        })

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
            holder.toDetailWithComment.setOnClickListener { v ->
                onCommentBtnClickListener?.onClick(v, position, holder)
            }
        }

        if (onItemClickListener != null) {
            holder.btnOption.setOnClickListener { v ->
                onItemClickListener?.onClick(v, position, holder)
            }
        }

        if (onLikeClickListener != null) {
            holder.btnLike.setOnClickListener { v ->
                onLikeClickListener?.onClick(v, position, holder)
            }
        }

        if (onShowDetailClickListener != null) {
            holder.toDetail.setOnClickListener { v ->
                onShowDetailClickListener?.onClick(v, position, holder)
            }
        }

        if (postList[position].like.contains(userName)) {
            holder.btnLike.setImageResource(R.drawable.ic_like)
        } else {
            holder.btnLike.setImageResource(R.drawable.ic_unlike)
        }

        if (selectedItem.get(position, false)) {
            val params = holder.textView.layoutParams.apply {
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                width = 0
            }
            holder.textView.layoutParams = params
            holder.btnShow.visibility = View.GONE
        } else {
            val params = holder.textView.layoutParams.apply {
                height = 60
                width = 0
            }
            holder.textView.layoutParams = params
        }

        if (postList[position].text.length < 20 || selectedItem.get(position, false)) {
            holder.showDetail = true
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
        private val viewPager: ViewPager2 = binding.viewPager
        val btnOption: ImageButton = binding.buttonOptions
        val btnLike: ImageButton = binding.ImageLike
        val toDetail: ConstraintLayout = binding.toDetail
        val toDetailWithComment: ImageButton = binding.showComment
        val likeCount: TextView = binding.likeCount

        var showDetail = false

        fun bind(item: Post) {
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


}