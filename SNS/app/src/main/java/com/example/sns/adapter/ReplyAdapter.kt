package com.example.sns.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.comment_item.view.owner_profile_image

class ReplyAdapter : RecyclerView.Adapter<ReplyAdapter.ReplyHolder>() {

    var replyList = ArrayList<Reply>()

    fun setReplyLIst(replyLIst : ArrayList<Reply>) {
        this.replyList.clear()
        this.replyList.addAll(replyLIst.also {
            it.forEach { data ->
                data.created_at = DateTimeConverter.jsonTimeToTime(data.created_at)
            }
        })
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReplyHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.reply_item, parent, false))

    override fun getItemCount() = replyList.size

    override fun onBindViewHolder(holder: ReplyHolder, position: Int) {

        holder.bind(replyList[position])
    }

    inner class ReplyHolder(private val binding: ReplyItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : Reply) {
            if (item.profile_image.profile_image != null) {
                itemView.run {
                    Glide.with(context).load(BASE_URL + item.profile_image.profile_image)
                        .apply(RequestOptions.circleCropTransform()).into(owner_profile_image)
                }
            }
            binding.item = item
        }
    }
}