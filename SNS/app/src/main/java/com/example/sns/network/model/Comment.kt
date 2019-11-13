package com.example.sns.network.model

data class Comment(
    val id: Int,
    val text: String,
    val reply: Int,
    val like: ArrayList<String>,
    var created_at: String,
    val owner: String,
    val post: Int,
    val profile_image: ProfileImage
)