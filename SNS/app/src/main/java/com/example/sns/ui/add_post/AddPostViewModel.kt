package com.example.sns.ui.add_post

import android.app.Application
import android.provider.MediaStore
import androidx.databinding.ObservableField
import com.example.sns.base.BaseViewModel
import com.example.sns.network.service.PostService
import com.example.sns.utils.UserObject
import kotlinx.coroutines.CoroutineScope
import okhttp3.MultipartBody
import android.app.Activity
import android.util.Log
import com.example.sns.utils.TokenObject


class AddPostViewModel(private val postService: PostService, application: Application) : BaseViewModel(application) {

    var file: ArrayList<MultipartBody.Part> = ArrayList()

    val text = ObservableField<String>()
    fun checkNetwork() = if (UserObject.userInfo.value != null) hasFile()  else makeToast("네트워크 연결 후 시도해 주세요")


    private fun hasFile() = if (file.isEmpty()) addPostWithoutFIle() else addPostWithFIle()

    private fun addPostWithFIle() = addDisposable(postService.addPostWithFile(TokenObject.token, text.get()!!, UserObject.userInfo.value?.user_id!!, file), getMsgObserver())

    private fun addPostWithoutFIle() = addDisposable(postService.addPostWithoutFile(TokenObject.token, text.get()!!, UserObject.userInfo.value?.user_id!!), getMsgObserver())

    fun getImageFromGallery(context: Activity): ArrayList<String> {
        val galleryImageUrls: ArrayList<String> = ArrayList()
        val columns = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID
        )//get all columns of type images
        val orderBy = MediaStore.Images.Media.DATE_TAKEN//order data by date

        val imageCursor = context.managedQuery(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
            null, null, "$orderBy DESC"
        )

        for (i in 0 until imageCursor.count) {
            imageCursor.moveToPosition(i)
            val dataColumnIndex =
                imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)//get column index
            galleryImageUrls.add(imageCursor.getString(dataColumnIndex))//get Image from column index

        }
        return galleryImageUrls
    }



}