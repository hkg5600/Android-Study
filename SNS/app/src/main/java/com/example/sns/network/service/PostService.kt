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
    fun getPost(follower: Follower): Single<retrofit2.Response<Response<PostList>>>

    fun addPostWithFile(text: String, userName: String, file: MultipartBody.Part): Single<retrofit2.Response<Response<Any>>>
    fun addPostWithoutFile(text: String, userName: String): Single<retrofit2.Response<Response<Any>>>
}

class PostServiceImpl(private val api: PostApi) : PostService {

    override fun addPostWithoutFile(text: String, userName: String) =
        api.addPostWithoutFile(PostRequest(text, userName))

    override fun addPostWithFile(text: String, userName: String, file: MultipartBody.Part) : Single<retrofit2.Response<Response<Any>>> {
        return api.addPostWithFile(file, RequestBody.create(MediaType.parse("text/plain"), text), RequestBody.create(MediaType.parse("text/plain"), userName))
    }

    override fun getPost(follower: Follower) = api.getPost(follower)


}