package com.example.sns.network.request

data class CommentRequest(val post: Int, val owner: String, val text: String)