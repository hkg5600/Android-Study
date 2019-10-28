package com.example.sns.ui.login

import android.app.Application
import androidx.databinding.ObservableField
import com.example.sns.base.BaseViewModel
import com.example.sns.network.service.LoginService
import com.example.sns.network.service.UserInfoService
import com.example.sns.room.model.Token

class LoginActivityViewModel(
    private val userInfoService: UserInfoService,
    private val loginService: LoginService,
    application: Application
) : BaseViewModel(application) {
    val id = ObservableField<String>()
    val pw = ObservableField<String>()

    fun checkValue() = if (id.get() != null && pw.get() != null) login() else null

    fun login() = addDisposable(loginService.login(id.get()!!, pw.get()!!), getDataObserver())

    fun insertToken(token: String) =
        addRoomDisposable(tokenRepository.insertToken(Token(1, token)), "token")


}