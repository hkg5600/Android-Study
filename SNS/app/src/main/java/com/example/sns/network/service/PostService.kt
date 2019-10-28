package com.example.sns.network.service

import android.util.Log
import com.example.sns.network.Response
import com.example.sns.network.api.PostApi
import com.example.sns.network.model.Follower
import com.example.sns.network.model.Post
import com.example.sns.network.model.addingPost
import com.example.sns.network.response.PostList
import io.reactivex.Single

interface PostService {
    fun getPost(follower: Follower): Single<retrofit2.Response<Response<PostList>>>
    fun addPost(post: addingPost): Single<retrofit2.Response<Response<Any>>>
}

class PostServiceImpl(private val api: PostApi) : PostService {

    override fun getPost(follower: Follower) = api.getPost(follower)


    override fun addPost(post: addingPost) = api.addPost(post)

}