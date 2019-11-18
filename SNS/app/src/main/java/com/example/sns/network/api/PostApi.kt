package com.example.sns.network.api

import com.example.sns.network.Response
import com.example.sns.network.request.*
import com.example.sns.network.response.PostDetail
import com.example.sns.network.response.PostLikeList
import com.example.sns.network.response.PostList
import com.example.sns.network.response.ReplyList
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface PostApi {
    @POST("/api/post/post/")
    fun getPost(@Header("Authorization") token: String, @Body post: GetPostRequest): Single<retrofit2.Response<Response<PostList>>>

    @Multipart
    @POST("/api/post/add_post/")
    fun addPost(
        @Header("Authorization") token: String, @Part file: ArrayList<MultipartBody.Part>, @Part(
            "inputText"
        ) text: RequestBody, @Part("owner") owner: RequestBody
    ): Single<retrofit2.Response<Response<Any>>>

    @DELETE
    fun deletePost(@Header("Authorization") token: String, @Url url: String): Single<retrofit2.Response<Response<Any>>>

    @GET
    fun getPostDetail(@Header("Authorization") token: String, @Url url: String): Single<retrofit2.Response<Response<PostDetail>>>

    @POST("/api/post/comment/")
    fun addComment(@Header("Authorization") token: String, @Body comment: CommentRequest): Single<retrofit2.Response<Response<Any>>>

    @GET
    fun likePost(@Header("Authorization") token: String, @Url url: String): Single<retrofit2.Response<Response<Any>>>

    @GET
    fun unlikePost(@Header("Authorization") token: String, @Url url: String): Single<retrofit2.Response<Response<Any>>>

    @DELETE
    fun deleteComment(@Header("Authorization") token: String, @Url url: String): Single<retrofit2.Response<Response<Any>>>

    @POST("api/post/user_profile_data/")
    fun getLike(@Header("Authorization") token: String, @Body post: PostId): Single<retrofit2.Response<Response<PostLikeList>>>

    @GET
    fun getReply(@Header("Authorization") token: String, @Url url: String): Single<retrofit2.Response<Response<ReplyList>>>

    @GET
    fun likeComment(@Header("Authorization") token: String, @Url url: String): Single<retrofit2.Response<Response<Any>>>

    @GET
    fun unLikeComment(@Header("Authorization") token: String, @Url url: String): Single<retrofit2.Response<Response<Any>>>

    @GET
    fun likeReply(@Header("Authorization") token: String, @Url url: String): Single<retrofit2.Response<Response<Any>>>

    @GET
    fun unLikeReply(@Header("Authorization") token: String, @Url url: String): Single<retrofit2.Response<Response<Any>>>

    @DELETE
    fun deleteReply(@Header("Authorization") token: String, @Url url: String): Single<retrofit2.Response<Response<Any>>>

    @POST("/api/post/reply/")
    fun addReply(@Header("Authorization") token: String, @Body reply: ReplyRequest): Single<retrofit2.Response<Response<Any>>>
}