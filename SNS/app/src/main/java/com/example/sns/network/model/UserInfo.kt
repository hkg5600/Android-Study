package com.example.sns.network.model

data class UserInfo(
    val user_id: String,
    val name: String,
    val followers: List<String>,
    val following: List<String>
)