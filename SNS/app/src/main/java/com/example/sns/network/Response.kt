package com.example.sns.network

data class Response<T>(val data: T, var status: Int)