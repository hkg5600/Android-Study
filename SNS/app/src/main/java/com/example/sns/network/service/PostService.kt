package com.example.sns.network.service

import android.util.Log
import com.example.sns.network.Response
import com.example.sns.network.api.PostApi
import com.example.sns.network.model.Post
import com.example.sns.room.model.Follower
import io.reactivex.Single

interface PostService {
    fun getPost() : Single<retrofit2.Response<Response<ArrayList<Post>>>>
}

class PostServiceImpl(private val api: PostApi) : PostService {
    override fun getPost(): Single<retrofit2.Response<Response<ArrayList<Post>>>> {
        val list = Follower(listOf("dgsw"))
        Log.d("Msg", "$list")
        return api.getPost(list)
    }

}