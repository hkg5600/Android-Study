package com.example.sns.ui.post_detail

import android.app.Application
import androidx.databinding.ObservableField
import com.example.sns.base.BaseViewModel
import com.example.sns.network.request.CommentRequest
import com.example.sns.network.response.PostDetail
import com.example.sns.network.service.PostService
import com.example.sns.utils.TokenObject
import com.example.sns.utils.UserObject

class PostDetailActivityViewModel(private val postService: PostService, application: Application) :
    BaseViewModel(application) {
    var id = 0
    var owner = ""
    var post : ObservableField<PostDetail> = ObservableField()
    var text = ObservableField<String>()
    fun getPostDetail() = addDisposable(postService.getPostDetail(TokenObject.token, id), getDataObserver())

    fun addComment() = addDisposable(postService.addComment(TokenObject.token, CommentRequest(id, UserObject.userInfo?.user?.user_id!!, text.get()!!)), getMsgObserver())

    fun deleteComment(id: Int) = addDisposable(postService.deleteComment(TokenObject.token, id), getMsgObserver())
}