package com.example.sns.ui.post

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sns.base.BaseViewModel
import com.example.sns.network.Response
import com.example.sns.network.model.Post
import com.example.sns.network.service.PostService
import com.example.sns.room.repository.TokenRepository
import com.example.sns.utils.SingleLiveEvent
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class PostViewModel(private val postService: PostService, application: Application) : BaseViewModel() {

    private val repository: TokenRepository = TokenRepository(application)
    val success: SingleLiveEvent<Any> = SingleLiveEvent()
    var liveData : MutableLiveData<ArrayList<Post>> = MutableLiveData()

    fun getPost() {
        Log.d("Msg", "in getPost Method")
        addDisposable(
            postService.getPost()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<retrofit2.Response<Response<ArrayList<Post>>>>() {
                    override fun onSuccess(t: retrofit2.Response<Response<ArrayList<Post>>>) {
                        if (t.isSuccessful) {
                            t.body()?.data.let {
                                liveData.value = it
                            }
                            Log.d("Data", "${liveData.value}")
                        }
                        success.call()
                    }

                    override fun onError(e: Throwable) {
                        Log.d("Error", "${e.message}")
                    }
                })
        )
    }


}