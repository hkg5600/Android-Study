package com.example.sns.network.api

import com.example.sns.network.Response
import com.example.sns.network.model.Post
import io.reactivex.Single
import retrofit2.http.GET

interface PostApi {
    @GET("/api/user/post/")
    fun getPost() : Single<retrofit2.Response<Response<ArrayList<Post>>>>
}