package com.example.sns.network.response

import com.example.sns.network.model.FollowerUser
import com.example.sns.network.model.FollowingUser
import com.example.sns.network.model.UserData

data class UserInfo(
    val user: UserData,
    val following: ArrayList<FollowingUser>,
    val followers: ArrayList<FollowerUser>
)