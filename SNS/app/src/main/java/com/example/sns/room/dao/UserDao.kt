package com.example.sns.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.sns.base.BaseDao
import com.example.sns.room.model.User
import io.reactivex.Single

@Dao
interface UserDao : BaseDao<User> {

    @Query("Select * From UserTable")
    fun getUserInfo() : Single<User>
}