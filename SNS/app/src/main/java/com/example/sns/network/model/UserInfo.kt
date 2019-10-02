package com.example.sns.network.model

data class UserInfo(
    val id: Int,
    val user_id: String,
    val name: String,
    val followers: List<Int>
)