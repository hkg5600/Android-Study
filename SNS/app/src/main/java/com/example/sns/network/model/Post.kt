package com.example.sns.network.model


data class Post(
    val id: Int,
    val text: String,
    val owner: String,
    var created_at: String,
    val images : ArrayList<PostImage>,
    val like : ArrayList<String>,
    val profile_image: ProfileImage
)