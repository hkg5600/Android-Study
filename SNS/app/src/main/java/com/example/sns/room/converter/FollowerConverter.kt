package com.example.sns.room.converter

import androidx.room.TypeConverter
import com.example.sns.room.model.Follower

class FollowerConverter {
    @TypeConverter
    fun dataToFollower(value: String) : Follower {
        return Follower(value.split("[",",","]").map { it.trim() })
    }

    @TypeConverter
    fun followerToData(follower: Follower) : String {
        return follower.user_id.joinToString(separator = ",", prefix = "", postfix = "")
    }
}