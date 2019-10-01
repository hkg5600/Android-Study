package com.example.sns.network.api

import com.example.sns.network.Response
import com.example.sns.network.model.Login
import com.example.sns.network.model.LoginData
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("/api/user/login/")
    fun login(@Body login : Login) : Single<retrofit2.Response<Response<LoginData>>>
}