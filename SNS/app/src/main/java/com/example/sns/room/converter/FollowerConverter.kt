package com.example.sns.room.converter

import androidx.room.TypeConverter
import com.example.sns.room.model.Follower
import java.util.*

class FollowerConverter {
    @TypeConverter
    fun dataToFollower(value: String) : Follower {
        return Follower(value.split("\\s*,\\s*"))
    }

    @TypeConverter
    fun followerToData(follower: Follower) : String {
        var value: String = ""
        follower.user_id.forEach {data ->
            value += "$data,"
        }
        return value
    }
}