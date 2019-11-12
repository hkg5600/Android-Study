package com.example.sns.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sns.R
import com.example.sns.databinding.UserListItemBinding
import com.example.sns.network.model.PostLike
import com.example.sns.network.response.PostLikeList
import com.example.sns.network.response.PostList
import com.example.sns.utils.BASE_URL
import kotlinx.android.synthetic.main.post_item.view.*
import kotlinx.android.synthetic.main.user_list_item.view.*

class LikeListAdapter : RecyclerView.Adapter<LikeListAdapter.LikeListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeListHolder = LikeListHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.user_list_item,
            parent,
            false
        ))

    override fun getItemCount() = likeUserList.size

    override fun onBindViewHolder(holder: LikeListHolder, position: Int) {

        holder.bind(likeUserList[position])
    }

    var likeUserList = ArrayList<PostLike>()

    fun setUserList(userList : ArrayList<PostLike>) {
        likeUserList = userList
        notifyDataSetChanged()
    }

    inner class LikeListHolder(private val binding : UserListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostLike) {
            if (item.profile_image != null) {
                itemView.run {
                    Glide.with(context).load(BASE_URL + item.profile_image)
                        .apply(RequestOptions.circleCropTransform()).into(user_profile_image)
                }
            }
            binding.item = item
        }
    }
}