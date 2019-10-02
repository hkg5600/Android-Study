package com.example.sns.network.api

import com.example.sns.network.Response
import com.example.sns.network.model.Post
import com.example.sns.room.model.Follower
import io.reactivex.Single
import retrofit2.http.*

interface PostApi {
    @POST("/api/user/post/")
    fun getPost(@Body follower: Follower) : Single<retrofit2.Response<Response<ArrayList<Post>>>>
}