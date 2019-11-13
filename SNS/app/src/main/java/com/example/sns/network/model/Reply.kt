package com.example.sns.network.model

data class Reply(
    val id: Int,
    val profile_image: ProfileImage,
    val text: String,
    var created_at: String,
    val comment: Int,
    val owner: String,
    val like: ArrayList<String>
)