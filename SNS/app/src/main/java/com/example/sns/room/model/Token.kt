package com.example.sns.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TokenTable")
data class Token(@PrimaryKey val id:Int, val token: String)