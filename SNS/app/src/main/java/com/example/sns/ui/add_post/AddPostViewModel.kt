package com.example.sns.ui.add_post

import android.app.Application
import android.util.Log
import com.example.sns.base.BaseViewModel
import com.example.sns.network.model.Post
import com.example.sns.network.model.addingPost
import com.example.sns.network.service.PostService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AddPostViewModel(private val postService: PostService, application: Application) :
    BaseViewModel(application) {

    fun addPost(title: String, text: String) {
        Log.d("Msg", "in addPost")
        addDisposable(postService.addPost(addingPost(title, text, user.value?.user_id!!))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it.isSuccessful)
                        success.call()
                },
                {
                    Log.d("Error", "${it.message}")
                    success.call()
                }
            ))
    }


}