package com.example.sns.network.service

import com.example.sns.network.Response
import com.example.sns.network.api.UserApi
import com.example.sns.network.model.UserInfo
import com.example.sns.room.model.Token
import io.reactivex.Single

interface UserInfoService {
    fun getUserInfo(token: Token) : Single<retrofit2.Response<Response<UserInfo>>>
}

class UserInfoServiceImpl(private val api: UserApi) : UserInfoService {
    override fun getUserInfo(token: Token): Single<retrofit2.Response<Response<UserInfo>>> {
        return api.getUserInfo("Token " + token.token)
    }
}