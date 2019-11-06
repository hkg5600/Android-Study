package com.example.sns.ui.post

import android.app.Application
import com.example.sns.base.BaseViewModel
import com.example.sns.network.model.Follower
import com.example.sns.network.service.PostService
import com.example.sns.network.service.UserInfoService
import com.example.sns.utils.TokenObject

class PostViewModel(
    private val service: UserInfoService,
    private val postService: PostService,
    application: Application
) :
    BaseViewModel(application) {

    fun getPost(followers: Follower) = addDisposable(postService.getPost(TokenObject.token, followers), getDataObserver())

    fun getUser() = addDisposable(service.getUserInfo(TokenObject.token), getDataObserver())

    fun deletePost(id: Int) = addDisposable(postService.deletePost(TokenObject.token,id), getMsgObserver())

    fun logout() = deleteToken()

    private fun deleteToken() = addRoomDisposable(tokenRepository.deleteToken(), "delete Token")

    fun likePost(id: Int) = addDisposable(postService.likePost(TokenObject.token, id), getMsgObserver())

    fun unlikePost(id: Int) = addDisposable(postService.likePost(TokenObject.token, id), getMsgObserver())

}