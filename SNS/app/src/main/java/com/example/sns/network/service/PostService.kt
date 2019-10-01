package com.example.sns.network.service

import com.example.sns.network.Response
import com.example.sns.network.api.PostApi
import com.example.sns.network.model.Post
import io.reactivex.Single

interface PostService {
    fun getPost() : Single<retrofit2.Response<Response<ArrayList<Post>>>>
}

class PostServiceImpl(private val api: PostApi) : PostService {
    override fun getPost(): Single<retrofit2.Response<Response<ArrayList<Post>>>> {
        return api.getPost()
    }

}