package com.example.sns.di

import com.example.sns.adapter.PostAdapter
import com.example.sns.network.api.PostApi
import com.example.sns.network.service.PostService
import com.example.sns.network.service.PostServiceImpl
import com.example.sns.ui.main.MainActivityViewModel
import com.example.sns.ui.post.PostViewModel
import com.example.sns.utils.BASE_URL
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


val retrofit: Retrofit = Retrofit
    .Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .build()

private val postApi : PostApi = retrofit.create(PostApi::class.java)

val networkModule = module {
    single { postApi }
}

var serviceModel = module {
    factory<PostService> {
        PostServiceImpl(get())
    }
}

var viewModelPart = module {
    viewModel { MainActivityViewModel() }
    viewModel { PostViewModel(get()) }
}

var adapterPart = module {
    factory {
        PostAdapter()
    }
}

var myDiModule = listOf(viewModelPart, networkModule, serviceModel, adapterPart)