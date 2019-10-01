package com.example.sns.di

import com.example.sns.adapter.PostAdapter
import com.example.sns.network.api.LoginApi
import com.example.sns.network.api.PostApi
import com.example.sns.network.api.UserApi
import com.example.sns.network.service.*
import com.example.sns.room.repository.TokenRepository
import com.example.sns.ui.login.LoginActivityViewModel
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
private val loginApi : LoginApi = retrofit.create(LoginApi::class.java)
private val userApi : UserApi = retrofit.create(UserApi::class.java)

val networkModule = module {
    single { postApi }
    single { loginApi }
    single { userApi }
}

var serviceModel = module {
    factory<PostService> {
        PostServiceImpl(get())
    }

    factory<LoginService> {
        LoginServiceImpl(get())
    }

    factory<UserInfoService> {
        UserInfoServiceImpl(get())
    }
}

var viewModelPart = module {
    viewModel { MainActivityViewModel(get(), get()) }
    viewModel { PostViewModel(get(), get()) }
    viewModel { LoginActivityViewModel(get(), get()) }
}

var adapterPart = module {
    factory {
        PostAdapter()
    }
}

var repositoryPart = module {
     factory {
         TokenRepository(get())
     }
}

var myDiModule = listOf(viewModelPart, networkModule, serviceModel, adapterPart, repositoryPart)