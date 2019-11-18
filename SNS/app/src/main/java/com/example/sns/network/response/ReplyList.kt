package com.example.sns.network.response

import com.example.sns.network.model.Reply

data class ReplyList(val next_count: Int, val nextPage: Int ,val reply: ArrayList<Reply>)