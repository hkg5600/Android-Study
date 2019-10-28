package com.example.sns.ui.post

import android.app.Application
import com.example.sns.base.BaseViewModel
import com.example.sns.network.model.Follower
import com.example.sns.network.service.PostService

class PostViewModel(private val postService: PostService, application: Application) :
    BaseViewModel(application) {

    fun getPost(followers: Follower) = addDisposable(postService.getPost(followers), getDataObserver())

}