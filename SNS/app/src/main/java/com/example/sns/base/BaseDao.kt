package com.example.sns.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import io.reactivex.Completable

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: T): Completable

    @Update
    fun update(entity: T): Completable

    @Delete
    fun delete(entity: T): Completable
}