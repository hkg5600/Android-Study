package com.example.sns.network.service

import com.example.sns.network.Response
import com.example.sns.network.api.PostApi
import com.example.sns.network.model.Follower
import com.example.sns.network.request.PostRequest
import com.example.sns.network.response.PostList
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface PostService {
    fun getPost(token: String, follower: Follower): Single<retrofit2.Response<Response<PostList>>>
    fun addPostWithFile(token: String, text: String, userName: String, file: ArrayList<MultipartBody.Part>): Single<retrofit2.Response<Response<Any>>>
    fun addPostWithoutFile(token: String, text: String, userName: String): Single<retrofit2.Response<Response<Any>>>
    fun deletePost(token: String,id: Int) : Single<retrofit2.Response<Response<Any>>>
}

class PostServiceImpl(private val api: PostApi) : PostService {

    override fun deletePost(token : String, id: Int): Single<retrofit2.Response<Response<Any>>> = api.deletePost(token,"/api/post/post/$id/")

    override fun addPostWithoutFile(token: String,  text: String, userName: String) =
        api.addPostWithoutFile(token, PostRequest(text, userName))

    override fun addPostWithFile(token: String,  text: String, userName: String, file: ArrayList<MultipartBody.Part>) =
        api.addPostWithFile(token, file, RequestBody.create(MediaType.parse("text/plain"), text), RequestBody.create(MediaType.parse("text/plain"), userName))

    override fun getPost(token : String, follower: Follower) = api.getPost(token, follower)

}