package com.example.sns.network.service

import android.util.Log
import com.example.sns.network.Response
import com.example.sns.network.api.PostApi
import com.example.sns.network.model.Follower
import com.example.sns.network.model.Post
import io.reactivex.Single

interface PostService {
    fun getPost(follower: List<String>): Single<retrofit2.Response<Response<ArrayList<Post>>>>
}

class PostServiceImpl(private val api: PostApi) : PostService {
    override fun getPost(follower: List<String>): Single<retrofit2.Response<Response<ArrayList<Post>>>> {
        val follower = Follower(follower)
        val validatedFollower = Follower(follower.user_id.filter {
            it != ""
        })
        return api.getPost(validatedFollower)
    }

}