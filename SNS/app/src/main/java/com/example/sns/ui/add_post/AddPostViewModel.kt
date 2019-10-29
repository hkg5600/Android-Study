package com.example.sns.ui.add_post

import android.app.Application
import androidx.databinding.ObservableField
import com.example.sns.base.BaseViewModel
import com.example.sns.network.service.PostService
import com.example.sns.utils.UserObject
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddPostViewModel(private val postService: PostService, application: Application) :
    BaseViewModel(application) {

    var file: MultipartBody.Part? = null

    val text = ObservableField<String>()

    fun checkData() = if (text.get() != null) hasFile() else null

    private fun hasFile() = if (file == null) addPostWithoutFIle() else addPostWithFIle()

//    private fun makeRequestBody() {
//        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), text.get()!!)
//        val multipartData = MultipartBody.Part.createFormData("text", requestFile)
//    }

    private fun addPostWithFIle() = addDisposable(postService.addPostWithFile(text.get()!!, UserObject.userInfo?.user_id!!, file!!), getMsgObserver())

    private fun addPostWithoutFIle() = addDisposable(postService.addPostWithoutFile(text.get()!!, UserObject.userInfo?.user_id!!), getMsgObserver())

}