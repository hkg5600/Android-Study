package com.example.sns.network.api

import com.example.sns.network.Response
import com.example.sns.network.model.Follower
import com.example.sns.network.request.PostRequest
import com.example.sns.network.response.PostList
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface PostApi {
    @POST("/api/post/post/")
    fun getPost(@Header("Authorization") token: String, @Body follower: Follower): Single<retrofit2.Response<Response<PostList>>>

    @POST("/api/post/add_post/")
    fun addPostWithoutFile(@Header("Authorization") token: String, @Body post: PostRequest): Single<retrofit2.Response<Response<Any>>>

    @Multipart
    @POST("/api/post/add_post/")
    fun addPostWithFile(@Header("Authorization") token: String, @Part file: ArrayList<MultipartBody.Part>, @Part("text") text: RequestBody, @Part("owner") owner: RequestBody): Single<retrofit2.Response<Response<Any>>>

    @DELETE
    fun deletePost(@Header("Authorization") token: String, @Url url: String) : Single<retrofit2.Response<Response<Any>>>
}