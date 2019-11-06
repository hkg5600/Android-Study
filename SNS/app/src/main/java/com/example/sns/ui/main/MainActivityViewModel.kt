package com.example.sns.ui.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.sns.base.BaseViewModel
import com.example.sns.network.service.UserInfoService
import com.example.sns.utils.TokenObject
import com.example.sns.utils.UserObject

class MainActivityViewModel(private val service: UserInfoService, application: Application) :
    BaseViewModel(application) {

    fun logout() {
        deleteUser()
        deleteToken()
    }

    private fun deleteUser() {
        UserObject.userInfo = null
    }

    private fun deleteToken() = addRoomDisposable(tokenRepository.deleteToken(), "delete Token")

}