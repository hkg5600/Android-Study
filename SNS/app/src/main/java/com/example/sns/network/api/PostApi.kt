package com.example.sns.network.api

import com.example.sns.network.Response
import com.example.sns.network.model.Follower
import com.example.sns.network.request.CommentRequest
import com.example.sns.network.request.GetPostRequest
import com.example.sns.network.request.PostId
import com.example.sns.network.request.PostRequest
import com.example.sns.network.response.CommentList
import com.example.sns.network.response.PostList
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface PostApi {
    @POST("/api/post/post/")
    fun getPost(@Header("Authorization") token: String, @Body post: GetPostRequest): Single<retrofit2.Response<Response<PostList>>>

    @Multipart
    @POST("/api/post/add_post/")
    fun addPost(@Header("Authorization") token: String, @Part file: ArrayList<MultipartBody.Part>, @Part("text") text: RequestBody, @Part("owner") owner: RequestBody): Single<retrofit2.Response<Response<Any>>>

    @DELETE
    fun deletePost(@Header("Authorization") token: String, @Url url: String) : Single<retrofit2.Response<Response<Any>>>

    @GET("/api/post/comment/")
    fun getComment(@Header("Authorization") token: String, @Body post:PostId): Single<retrofit2.Response<Response<CommentList>>>

    @POST("/api/post/comment/")
    fun addComment(@Header("Authorization") token: String, @Body comment:CommentRequest): Single<retrofit2.Response<Response<Any>>>

    @POST("/api/post/like_post/")
    fun likePost(@Header("Authorization") token: String, @Body post:PostId): Single<retrofit2.Response<Response<Any>>>

    @POST("/api/post/unlike_post/")
    fun unlikePost(@Header("Authorization") token: String, @Body post:PostId): Single<retrofit2.Response<Response<Any>>>
}