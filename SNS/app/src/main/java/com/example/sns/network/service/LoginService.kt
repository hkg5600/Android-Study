package com.example.sns.network.service

import com.example.sns.network.Response
import com.example.sns.network.api.LoginApi
import com.example.sns.network.model.Login
import com.example.sns.network.model.LoginData
import io.reactivex.Single

interface LoginService {
    fun login(id: String, pw: String): Single<retrofit2.Response<Response<LoginData>>>
}

class LoginServiceImpl(private val api: LoginApi) : LoginService {
    override fun login(id: String, pw: String) = api.login(Login(id, pw))

}