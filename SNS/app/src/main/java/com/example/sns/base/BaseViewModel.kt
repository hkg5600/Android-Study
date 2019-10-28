package com.example.sns.base

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sns.network.Response
import com.example.sns.room.model.Token
import com.example.sns.room.repository.TokenRepository
import com.example.sns.utils.SingleLiveEvent
import com.example.sns.utils.TokenObject
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

abstract class BaseViewModel(application: Application) : ViewModel() {
    val tokenRepository = TokenRepository(application)
    val success: SingleLiveEvent<Any> = SingleLiveEvent()
    private val compositeDisposable = CompositeDisposable()
    val roomSuccess: SingleLiveEvent<String> = SingleLiveEvent()
    val error: SingleLiveEvent<String> = SingleLiveEvent()
    val message: MutableLiveData<String> = MutableLiveData()
    val data: MutableLiveData<Any> = MutableLiveData()

    fun addDisposable(disposable: Single<*>, observer: DisposableSingleObserver<Any>) {
        compositeDisposable.add(
            disposable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(observer)
        )
    }

    fun addRoomDisposable(disposable: Completable, msg: String) {
        compositeDisposable.add(
            disposable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    roomSuccess.value = msg
                },
                { error.value = "failed to insert" })
        )
    }

    fun getMsgObserver() = MsgDisposableSingleObserver()
    fun getDataObserver() = DataDisposableSingleObserver()

    inner class MsgDisposableSingleObserver : DisposableSingleObserver<Any>() {

        override fun onSuccess(t: Any) = filterResponseWithMsg(t)

        override fun onError(e: Throwable) {
            Log.d("Error", "${e.message}")
            error.value = "error"
        }

    }

    inner class DataDisposableSingleObserver : DisposableSingleObserver<Any>() {

        override fun onSuccess(t: Any) = filterResponseWithData(t)

        override fun onError(e: Throwable) {
            Log.d("Error", "${e.message}")
            error.value = "error"
        }

    }

    fun tokenDisposable() {
        compositeDisposable.add(
            tokenRepository.getToken().subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribeWith(TokenDisposableSingleObserver())
        )
    }

    inner class TokenDisposableSingleObserver : DisposableSingleObserver<Token>() {
        override fun onSuccess(t: Token) {
            Log.d("Success", "${t.token}")
            TokenObject.token = t.token
            success.call()
        }

        override fun onError(e: Throwable) {
            Log.d("Error", "${e.message}")
            success.call()
        }
    }

    fun filterResponseWithMsg(t: Any) {
        t as retrofit2.Response<Response<String>>
        if (t.isSuccessful) {
            if (t.body()?.status == 200)
                message.value = t.body()?.message!!
            else error.value = t.body()?.message!!
        } else {
            Log.d("Error Body", t.errorBody().toString())
            error.value = t.errorBody().toString()
        }
    }

    fun filterResponseWithData(t: Any) {
        t as retrofit2.Response<Response<*>>
        if (t.isSuccessful) {
            if (t.body()?.status == 200) {
                data.value = t.body()?.data!!
            } else
                error.value = t.body()?.message!!
        } else {
            Log.d("Error Body", t.errorBody().toString())
            error.value = t.errorBody().toString()
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}