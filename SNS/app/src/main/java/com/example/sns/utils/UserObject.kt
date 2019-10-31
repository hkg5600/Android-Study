package com.example.sns.utils

import androidx.lifecycle.MutableLiveData
import com.example.sns.network.model.UserInfo

object UserObject {

    //var userInfo : UserInfo? = null
    var userInfo : MutableLiveData<UserInfo>? = MutableLiveData()
}