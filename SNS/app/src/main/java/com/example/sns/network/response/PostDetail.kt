package com.example.sns.network.response

import com.example.sns.network.model.Comment
import com.example.sns.network.model.Post
import com.example.sns.network.model.PostImage
import com.example.sns.network.model.ProfileImage

data class PostDetail(
    val last_page: Boolean,
    val nextPage: Int,
    val post:Post,
    val comment: ArrayList<Comment>
)