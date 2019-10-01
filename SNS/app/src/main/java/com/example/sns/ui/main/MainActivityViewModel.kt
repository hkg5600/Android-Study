package com.example.sns.ui.main

import android.app.Application
import android.util.Log
import com.example.sns.base.BaseViewModel
import com.example.sns.network.Response
import com.example.sns.network.model.UserInfo
import com.example.sns.network.service.UserInfoService
import com.example.sns.room.model.Token
import com.example.sns.room.model.User
import com.example.sns.room.repository.TokenRepository
import com.example.sns.room.repository.UserRepository
import com.example.sns.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainActivityViewModel(private val service: UserInfoService, application: Application) :
    BaseViewModel() {
    private val tokenRepository: TokenRepository = TokenRepository(application)
    private val userRepository: UserRepository = UserRepository(application)
    val succeess: SingleLiveEvent<Any> = SingleLiveEvent()

    fun getToken() {
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
            userRepository.insertUser(User(user_id = userInfo.user_id, id = userInfo.id, name = userInfo.name))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.d("Success", "success to insert User Info")
                        succeess.call()
                    },
                    { error -> Log.d("Error insert User", "${error.message}")}
                )
        )
    }
}