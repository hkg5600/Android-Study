package com.example.sns.network.response

import com.example.sns.network.model.Comment
import com.example.sns.network.model.PostImage
import com.example.sns.network.model.ProfileImage

data class PostDetail(
    val id: Int,
    val text: String,
    val owner: String,
    var created_at: String,
    val like: ArrayList<String>,
    val profile_image: ProfileImage,
    val comments: ArrayList<Comment>
)