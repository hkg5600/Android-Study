package com.example.sns.ui.login

import android.app.Application
import android.util.Log
import com.example.sns.base.BaseViewModel
import com.example.sns.network.Response
import com.example.sns.network.model.LoginData
import com.example.sns.network.service.LoginService
import com.example.sns.room.model.Token
import com.example.sns.room.repository.TokenRepository
import com.example.sns.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class LoginActivityViewModel(private val loginService: LoginService, application: Application) : BaseViewModel() {
    private val repository: TokenRepository = TokenRepository(application)
    val success : SingleLiveEvent<Any> = SingleLiveEvent()

    fun login(id: String, pw: String) {
        addDisposable(
            loginService.login(id, pw)
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
            repository.insertToken(Token(1, token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.d("Success", "success to insert token")
                        success.call()
                    },
                    { error -> Log.d("Error", "Error while insert token ${error.message}") }
                )
        )
    }



}