package com.example.sns.room.repository

import android.app.Application
import com.example.sns.room.dao.TokenDao
import com.example.sns.room.database.TokenDatabase
import com.example.sns.room.model.Token
import io.reactivex.Completable
import io.reactivex.Single

class TokenRepository(application: Application) {
    private var tokenDao : TokenDao

    init {
        val database = TokenDatabase.getInstance(application)!!
        tokenDao = database.tokenDao()
    }

    fun getToken() : Single<Token> {
        return tokenDao.getToken()
    }

    fun insertToken(entity: Token): Completable {
        return tokenDao.insert(entity)
    }

}