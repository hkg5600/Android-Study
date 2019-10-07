package com.example.sns.network.service

import android.util.Log
import com.example.sns.network.Response
import com.example.sns.network.api.PostApi
import com.example.sns.network.model.Follower
import com.example.sns.network.model.Post
import com.example.sns.network.model.addingPost
import io.reactivex.Single

interface PostService {
    fun getPost(follower: List<String>): Single<retrofit2.Response<Response<ArrayList<Post>>>>
    fun addPost(post: addingPost) : Single<retrofit2.Response<Any>>
}

class PostServiceImpl(private val api: PostApi) : PostService {

    override fun getPost(follower: List<String>): Single<retrofit2.Response<Response<ArrayList<Post>>>> {
        return api.getPost(Follower(follower.filter {
            it != ""
        }))
    }

    override fun addPost(post: addingPost): Single<retrofit2.Response<Any>> {
        return api.addPost(post)
    }

}