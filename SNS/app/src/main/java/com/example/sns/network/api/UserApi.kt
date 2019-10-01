package com.example.sns.network.api

import com.example.sns.network.Response
import com.example.sns.network.model.UserInfo
import com.example.sns.room.model.Token
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface UserApi {

    @GET("api/user/user/")
    fun getUserInfo(@Header("Authorization") token: String) : Single<retrofit2.Response<Response<UserInfo>>>

}