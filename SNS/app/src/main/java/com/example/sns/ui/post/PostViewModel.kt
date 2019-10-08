package com.example.sns.ui.post

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sns.base.BaseViewModel
import com.example.sns.network.Response
import com.example.sns.network.model.Post
import com.example.sns.network.service.PostService
import com.example.sns.room.model.User
import com.example.sns.room.repository.TokenRepository
import com.example.sns.room.repository.UserRepository
import com.example.sns.utils.SingleLiveEvent
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class PostViewModel(private val postService: PostService, application: Application) :
    BaseViewModel(application) {

    var liveData: ObservableArrayList<Post> = ObservableArrayList()

    fun getPost(followers : List<String>) {
        Log.d("Msg", "in getPost Method")
        addDisposable(
            postService.getPost(followers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<retrofit2.Response<Response<ArrayList<Post>>>>() {
                    override fun onSuccess(t: retrofit2.Response<Response<ArrayList<Post>>>) {
                        if (t.isSuccessful) {
                            t.body()?.data?.let { data ->
                                data.forEach {
                                    liveData.add(it)
                                }
                            }
                        }
                        success.call()
                    }

                    override fun onError(e: Throwable) {
                        Log.d("Error", "${e.message}")
                    }
                })
        )
    }

//    override fun getUserInfo() {
//        Log.d("Msg", "in get user info")
//        addDisposable(
//            repository.getUser()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(object : DisposableSingleObserver<User>() {
//                    override fun onSuccess(t: User) {
//                        getPost(t.followers.user_id)
//                    }
//
//                    override fun onError(e: Throwable) {
//
//                    }
//                })
//        )
//    }


}