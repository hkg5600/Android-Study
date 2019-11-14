package com.example.sns.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object DateTimeConverter {
    @SuppressLint("SimpleDateFormat")
    fun jsonTimeToTime(jsonTime : String) : String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm")
        return formatter.format(parser.parse(jsonTime)!!)
    }
}