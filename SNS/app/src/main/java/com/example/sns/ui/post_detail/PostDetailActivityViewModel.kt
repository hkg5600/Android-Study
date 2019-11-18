package com.example.sns.ui.post_detail

import android.app.Application
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.style.ClickableSpan
import androidx.databinding.ObservableField
import com.example.sns.base.BaseViewModel
import com.example.sns.network.request.CommentRequest
import com.example.sns.network.request.ReplyRequest
import com.example.sns.network.response.PostDetail
import com.example.sns.network.service.PostService
import com.example.sns.utils.TokenObject
import com.example.sns.utils.UserObject

class PostDetailActivityViewModel(private val postService: PostService, application: Application) :
    BaseViewModel(application) {
    var postId = 0
    var commentId = 0
    var post: ObservableField<PostDetail> = ObservableField()
    var isReplyAdd = false
    var spannableText = ObservableField<SpannableString>()

    lateinit var postText: SpannableString
    var inputText = ObservableField<String>()

    fun isReplyOrComment() = if (isReplyAdd) addReply() else addComment()

    fun getPostDetail(id: Int, page: Int) = addDisposable(postService.getPostDetail(TokenObject.token, id, page), getDataObserver())

    private fun addComment() = addDisposable(postService.addComment(TokenObject.token, CommentRequest(postId, UserObject.userInfo?.user?.user_id!!, "<strong>${UserObject.userInfo?.user?.user_id!!}</strong>  ${inputText.get()!!}")), getMsgObserver())

    private fun addReply() = addDisposable(postService.addReply(TokenObject.token, ReplyRequest("<strong>${UserObject.userInfo?.user?.user_id!!}</strong>  ${inputText.get()!!}",commentId ,UserObject.userInfo?.user?.user_id!!)), getMsgObserver())

    fun deleteComment(id: Int) = addDisposable(postService.deleteComment(TokenObject.token, id), getMsgObserver())

    fun getReply(id: Int, page: Int) = addDisposable(postService.getReply(TokenObject.token, id, page), getDataObserver())

    fun likeComment(id: Int) = addDisposable(postService.likeComment(TokenObject.token, id), getMsgObserver())

    fun unLikeComment(id: Int) = addDisposable(postService.unLikeComment(TokenObject.token, id), getMsgObserver())

    fun likeReply(id: Int) = addDisposable(postService.likeReply(TokenObject.token, id), getMsgObserver())

    fun unLikeReply(id: Int) = addDisposable(postService.unLikeReply(TokenObject.token, id), getMsgObserver())

    fun deleteReply(id: Int) = addDisposable(postService.deleteReply(TokenObject.token, id), getMsgObserver())

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

}