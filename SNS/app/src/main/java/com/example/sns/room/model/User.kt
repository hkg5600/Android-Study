package com.example.sns.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.sns.room.converter.FollowerConverter

@Entity(tableName = "UserTable")
data class User(
    @PrimaryKey val id: Int,
    val user_id: String,
    val name: String,
    @TypeConverters(FollowerConverter::class)
    val followers: Follower
)