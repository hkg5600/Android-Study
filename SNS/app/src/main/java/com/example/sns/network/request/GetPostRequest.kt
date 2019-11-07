package com.example.sns.network.request

import com.example.sns.network.model.Follower

data class GetPostRequest(val page: Int, val user_id: List<String>)