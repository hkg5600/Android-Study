package com.example.sns.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.sns.base.BaseViewModel
import com.example.sns.network.Response
import com.example.sns.network.model.UserInfo
import com.example.sns.network.service.UserInfoService
import com.example.sns.room.model.Follower
import com.example.sns.room.model.Token
import com.example.sns.room.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainActivityViewModel(private val service: UserInfoService, application: Application) :
    BaseViewModel(application) {

    var logoutSuccess : MutableLiveData<Boolean> = MutableLiveData()

    override fun getToken() {
        addDisposable(
            tokenRepository.getToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Token>() {
                    override fun onSuccess(t: Token) {
                        Log.d("Token", t.token)
                        getUser(t)
                    }

                    override fun onError(e: Throwable) {
                        Log.d("Error get Token", "${e.message}")
                    }
                })
        )
    }

    fun getUser(token: Token) {
        addDisposable(
            service.getUserInfo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<retrofit2.Response<Response<UserInfo>>>() {
                    override fun onSuccess(t: retrofit2.Response<Response<UserInfo>>) {
                        if (t.isSuccessful) {
                            t.body()?.data?.let {
                                Log.d("Msg", "get user")
                                insertUser(it)
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.d("Error get User", "${e.message}")
                    }
                })

        )
    }

    fun insertUser(userInfo: UserInfo) {
        addDisposable(
            userRepository.insertUser(User(user_id = userInfo.user_id, id = userInfo.id, name = userInfo.name, followers = Follower(listOf(userInfo.followers.toString()))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.d("Success", "success to insert User Info")
                        success.call()
                    },
                    { error -> Log.d("Error insert User", "${error.message}")}
                )
        )
    }

    fun logout() {
        addDisposable(
            tokenRepository.deleteToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        logoutSuccess.postValue(true)
                    },
                    {
                        Log.d("Error", "${it.message}")
                    }
                )
        )
    }
}