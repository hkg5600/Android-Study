package com.example.sns.ui.userInfo

import android.app.Application
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import com.example.sns.base.BaseViewModel
import com.example.sns.room.model.Follower

class UserInfoViewModel(application: Application) : BaseViewModel(application) {

    data class UserInfo(val userName: String, val follower: Follower)

    var userInfo : ObservableField<UserInfo> = ObservableField()

    fun getUser() {
        userInfo.set(UserInfo(getUserName(), getFollower()))
        success.call()
    }

    private fun getUserName() : String {
        return user.value?.user_id!!
    }

    private fun getFollower() : Follower {

        return Follower(user.value?.followers?.user_id!!.filter {
            it != ""
        })
    }

    private fun getFollowing() {

    }

}