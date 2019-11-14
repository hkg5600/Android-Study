package com.example.sns.ui.post_detail

import android.app.Application
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.style.ClickableSpan
import androidx.core.text.HtmlCompat
import androidx.databinding.ObservableField
import com.example.sns.base.BaseViewModel
import com.example.sns.network.request.CommentRequest
import com.example.sns.network.response.PostDetail
import com.example.sns.network.service.PostService
import com.example.sns.utils.TokenObject
import com.example.sns.utils.UserObject

class PostDetailActivityViewModel(private val postService: PostService, application: Application) :
    BaseViewModel(application) {
    var id = 0
    var post: ObservableField<PostDetail> = ObservableField()

    var spannableText = ObservableField<SpannableString>()

    lateinit var postText: SpannableString

    fun setText(complete : String, clickableSpan: ClickableSpan, start: Int, end: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            postText = SpannableString(Html.fromHtml(complete, Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE))
        } else {
            @Suppress("DEPRECATION")
            postText = SpannableString(Html.fromHtml(complete))
        }
        postText.setSpan(clickableSpan, start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableText.set(postText)
    }


    var text = ObservableField<String>()

    fun getPostDetail(id: Int, page: Int) =
        addDisposable(postService.getPostDetail(TokenObject.token, id, page), getDataObserver())

    fun addComment() = addDisposable(
        postService.addComment(
            TokenObject.token,
            CommentRequest(
                id,
                UserObject.userInfo?.user?.user_id!!,
                "<strong>${UserObject.userInfo?.user?.user_id!!}</strong>  ${text.get()!!}"
            )
        ), getMsgObserver()
    )

    fun deleteComment(id: Int) =
        addDisposable(postService.deleteComment(TokenObject.token, id), getMsgObserver())

    fun getReply(id: Int) =
        addDisposable(postService.getReply(TokenObject.token, id), getDataObserver())

    fun likeComment(id: Int) =
        addDisposable(postService.likeComment(TokenObject.token, id), getMsgObserver())

    fun unLikeComment(id: Int) =
        addDisposable(postService.unLikeComment(TokenObject.token, id), getMsgObserver())
}