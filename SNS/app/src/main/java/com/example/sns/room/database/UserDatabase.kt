package com.example.sns.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.sns.room.converter.FollowerConverter
import com.example.sns.room.dao.UserDao
import com.example.sns.room.model.User
import org.koin.core.instance.holder.Instance


@Database(entities = [User::class], version = 8, exportSchema = false)
@TypeConverters(FollowerConverter::class)
abstract class UserDatabase : RoomDatabase(){

    abstract fun userDao() : UserDao

    companion object {
        private var INSTANCE : UserDatabase? = null

        @Synchronized
        fun getInstance(context: Context): UserDatabase? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    UserDatabase::class.java, "user_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }
    }
}