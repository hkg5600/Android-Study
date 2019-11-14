package com.example.sns.ui.post_detail

import android.app.Application
import com.example.sns.base.BaseViewModel
import com.example.sns.network.service.PostService
import com.example.sns.utils.TokenObject

class PostLikeActivityViewModel(private val postService: PostService, application: Application) : BaseViewModel(application) {

    fun getLike(id: Int) = addDisposable(postService.getLikePost(TokenObject.token, id), getDataObserver())
}