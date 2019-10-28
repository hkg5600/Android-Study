package com.example.sns.network.api

import com.example.sns.network.Response
import com.example.sns.network.model.Follower
import com.example.sns.network.model.Post
import com.example.sns.network.model.addingPost
import com.example.sns.network.response.PostList
import io.reactivex.Single
import retrofit2.http.*

interface PostApi {
    @POST("/api/user/post/")
    fun getPost(@Body follower: Follower) : Single<retrofit2.Response<Response<PostList>>>

    @POST("/api/user/add_post/")
    fun addPost(@Body post: addingPost) : Single<retrofit2.Response<Response<Any>>>
}