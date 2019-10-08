package com.example.sns.room.repository

import android.app.Application
import com.example.sns.room.dao.TokenDao
import com.example.sns.room.dao.UserDao
import com.example.sns.room.database.TokenDatabase
import com.example.sns.room.database.UserDatabase
import com.example.sns.room.model.Token
import com.example.sns.room.model.User
import io.reactivex.Completable
import io.reactivex.Single

class UserRepository(application: Application) {
    private var userDao : UserDao

    init {
        val database = UserDatabase.getInstance(application)!!
        userDao = database.userDao()
    }

    fun getUser() : Single<User> {
        return userDao.getUserInfo()
    }

    fun insertUser(entity: User): Completable {
        return userDao.insert(entity)
    }

    fun deleteUesr() : Completable {
        return userDao.deleteUser()
    }
}