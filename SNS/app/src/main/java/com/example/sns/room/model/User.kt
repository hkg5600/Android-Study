package com.example.sns.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserTable")
data class User(
    @PrimaryKey val id: Int,
    val user_id: String,
    val name: String
)