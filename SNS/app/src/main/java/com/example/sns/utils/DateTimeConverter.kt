package com.example.sns.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateTimeConverter {
    @RequiresApi(Build.VERSION_CODES.O)
    fun jsonTimeToTime(jsonTime : String) : LocalDate {
        return LocalDate.parse(jsonTime, DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))
    }
}