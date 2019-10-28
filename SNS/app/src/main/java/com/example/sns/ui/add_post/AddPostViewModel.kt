package com.example.sns.ui.add_post

import android.app.Application
import androidx.databinding.ObservableField
import com.example.sns.base.BaseViewModel
import com.example.sns.network.model.addingPost
import com.example.sns.network.service.PostService
import com.example.sns.utils.UserObject

class AddPostViewModel(private val postService: PostService, application: Application) :
    BaseViewModel(application) {


    val title = ObservableField<String>()
    val text = ObservableField<String>()

    fun checkData() = if (title.get() != null && text.get() != null) addPost() else null

    private fun addPost() = addDisposable(postService.addPost(addingPost(title.get()!!, text.get()!!, UserObject.userInfo?.user_id!!)), getMsgObserver())

}