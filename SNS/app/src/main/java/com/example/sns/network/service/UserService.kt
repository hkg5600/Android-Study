package com.example.sns.network.service

import com.example.sns.network.Response
import com.example.sns.network.api.UserApi
import com.example.sns.network.response.UserInfo
import io.reactivex.Single

interface UserInfoService {
    fun getUserInfo(token: String) : Single<retrofit2.Response<Response<UserInfo>>>
}

class UserInfoServiceImpl(private val api: UserApi) : UserInfoService {
    override fun getUserInfo(token: String) = api.getUserInfo(token)
}