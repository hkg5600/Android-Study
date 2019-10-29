package com.example.sns.network.model


data class Post(
    val id: Int,
    val text: String,
    val owner: String,
    val created_at: String,
    val image: String?
)