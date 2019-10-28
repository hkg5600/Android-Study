package com.example.sns.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object DateTimeConverter {
    fun jsonTimeToTime(jsonTime : String) : String {
        val parser = SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm")
        return formatter.format(parser.parse(jsonTime))
    }
}