package com.example.sns.base

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sns.room.model.Token
import com.example.sns.room.model.User
import com.example.sns.room.repository.TokenRepository
import com.example.sns.room.repository.UserRepository
import com.example.sns.utils.SingleLiveEvent
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

abstract class BaseViewModel(application: Application) : ViewModel() {
    val tokenRepository = TokenRepository(application)
    val userRepository = UserRepository(application)
    val success: SingleLiveEvent<Any> = SingleLiveEvent()
    private val compositeDisposable = CompositeDisposable()
    var token: MutableLiveData<String> = MutableLiveData()
    var user: MutableLiveData<User> = MutableLiveData()

    init {
        getToken()
        getUserInfo()
    }

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        Log.d("Msg", "onCleared")
        compositeDisposable.clear()
        super.onCleared()
    }

    open fun getToken() {
        addDisposable(
            tokenRepository.getToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Token>() {
                    override fun onSuccess(t: Token) {
                        Log.d("Token", t.token)
                        token.postValue(t.token)
                    }

                    override fun onError(e: Throwable) {
                        Log.d("Error get Token", "${e.message}")
                        token.postValue("")
                    }
                })
        )
    }

    fun getUserInfo() {
        Log.d("Msg", "in get user info")
        addDisposable(
            userRepository.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<User>() {
                    override fun onSuccess(t: User) {
                        user.postValue(t)
                    }

                    override fun onError(e: Throwable) {
                        Log.d("Error", "${e.message}")
                    }
                })
        )
    }
}