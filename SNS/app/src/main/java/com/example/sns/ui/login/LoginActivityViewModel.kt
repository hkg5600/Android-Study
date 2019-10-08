package com.example.sns.ui.login

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import com.example.sns.base.BaseViewModel
import com.example.sns.network.Response
import com.example.sns.network.model.LoginData
import com.example.sns.network.model.UserInfo
import com.example.sns.network.service.LoginService
import com.example.sns.network.service.UserInfoService
import com.example.sns.room.model.Follower
import com.example.sns.room.model.Token
import com.example.sns.room.model.User
import com.example.sns.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class LoginActivityViewModel(private val service: UserInfoService, private val loginService: LoginService, application: Application) : BaseViewModel(application) {

    val id = ObservableField<String>()
    val pw = ObservableField<String>()

    fun login() {
        addDisposable(
            loginService.login(id.get()!!, pw.get()!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<retrofit2.Response<Response<LoginData>>>() {
                    override fun onSuccess(t: retrofit2.Response<Response<LoginData>>) {
                        if (t.isSuccessful) {
                                t.body()?.data.let {
                                    Log.d("Data", "${it?.token}")
                                    it?.let {
                                        insertToken(it.token)
                                    }
                                }
                        }
                    }
                    override fun onError(e: Throwable) {
                        Log.d("Error", "${e.message}")
                    }
                })
        )
    }

    fun insertToken(token: String) {
        Log.d("Msg", "in insertToken viewModel")

        addDisposable(
            tokenRepository.insertToken(Token(1, token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.d("Success", "success to insert token")
                        getUser(Token(1, token))
                    },
                    { error -> Log.d("Error", "Error while insert token ${error.message}") }
                )
        )
    }

    private fun getUser(token: Token) {
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
}