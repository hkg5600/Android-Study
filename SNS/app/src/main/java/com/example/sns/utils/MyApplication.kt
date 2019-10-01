package com.example.sns.utils

import android.app.Application
import com.example.sns.di.myDiModule
import org.koin.android.ext.android.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, myDiModule)
    }
}