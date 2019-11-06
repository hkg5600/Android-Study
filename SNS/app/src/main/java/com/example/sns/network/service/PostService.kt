package com.example.sns.network.service

import com.example.sns.network.Response
import com.example.sns.network.api.PostApi
import com.example.sns.network.model.Follower
import com.example.sns.network.request.CommentRequest
import com.example.sns.network.response.CommentList
import com.example.sns.network.response.PostList
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface PostService {
    fun getPost(token: String, follower: Follower): Single<retrofit2.Response<Response<PostList>>>
    fun addPost(token: String, text: String, userName: String, file: ArrayList<MultipartBody.Part>): Single<retrofit2.Response<Response<Any>>>
    fun deletePost(token: String,id: Int) : Single<retrofit2.Response<Response<Any>>>
    fun getComment(token: String, post: Int) : Single<retrofit2.Response<Response<CommentList>>>
    fun addComment(token: String, comment: CommentRequest) : Single<retrofit2.Response<Response<Any>>>
}

class PostServiceImpl(private val api: PostApi) : PostService {
    override fun addComment(token: String, comment: CommentRequest) = api.addComment(token, comment)

    override fun getComment(token: String, post: Int) = api.getComment(token, post)

    override fun deletePost(token : String, id: Int): Single<retrofit2.Response<Response<Any>>> = api.deletePost(token,"/api/post/post/$id/")

    override fun addPost(token: String, text: String, userName: String, file: ArrayList<MultipartBody.Part>) =
        api.addPost(token, file, RequestBody.create(MediaType.parse("text/plain"), text), RequestBody.create(MediaType.parse("text/plain"), userName))

    override fun getPost(token : String, follower: Follower) = api.getPost(token, follower)

}