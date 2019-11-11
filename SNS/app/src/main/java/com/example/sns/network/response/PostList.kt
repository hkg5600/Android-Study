package com.example.sns.network.response

import com.example.sns.network.model.Post

data class PostList(val last_page: Boolean, val nextPage: Int, val post: ArrayList<Post>)