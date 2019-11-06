package com.example.sns.network.model

data class Comment(
    val id: Int,
    val text: Int,
    val like: ArrayList<String>,
    val created_at: String,
    val owner: String,
    val post: Int
)