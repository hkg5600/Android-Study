package com.example.sns.ui.userInfo

import android.app.Application
import androidx.databinding.ObservableField
import com.example.sns.base.BaseViewModel
import com.example.sns.network.model.Follower
import com.example.sns.network.model.UserInfo

class UserInfoViewModel(application: Application) : BaseViewModel(application) {

    var userInfo : ObservableField<UserInfo> = ObservableField()

}