package com.example.sns.network.model

import java.time.format.DateTimeFormatter

data class Post(val id: Int, val title: String, val text: String, val owner: String ,val created_at: String)