package com.example.sns.network.service

import com.example.sns.network.Response
import com.example.sns.network.api.PostApi
import com.example.sns.network.model.Follower
import com.example.sns.network.request.*
import com.example.sns.network.response.PostDetail
import com.example.sns.network.response.PostLikeList
import com.example.sns.network.response.PostList
import com.example.sns.network.response.ReplyList
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface PostService {
    fun getPost(token: String, follower: Follower, page : Int): Single<retrofit2.Response<Response<PostList>>>
    fun addPost(token: String, text: String, userName: String, file: ArrayList<MultipartBody.Part>): Single<retrofit2.Response<Response<Any>>>
    fun deletePost(token: String,id: Int) : Single<retrofit2.Response<Response<Any>>>
    fun getPostDetail(token: String, id:Int, page :Int) : Single<retrofit2.Response<Response<PostDetail>>>
    fun addComment(token: String, comment: CommentRequest) : Single<retrofit2.Response<Response<Any>>>
    fun likePost(token: String, id: Int) : Single<retrofit2.Response<Response<Any>>>
    fun unlikePost(token: String, id: Int) : Single<retrofit2.Response<Response<Any>>>
    fun deleteComment(token: String, id: Int) : Single<retrofit2.Response<Response<Any>>>
    fun getLikePost(token: String, id: Int) : Single<retrofit2.Response<Response<PostLikeList>>>
    fun getReply(token: String, id: Int, page: Int) : Single<retrofit2.Response<Response<ReplyList>>>
    fun unLikeComment(token: String, id: Int) : Single<retrofit2.Response<Response<Any>>>
    fun likeComment(token: String, id: Int) : Single<retrofit2.Response<Response<Any>>>
    fun likeReply(token: String, id: Int) : Single<retrofit2.Response<Response<Any>>>
    fun unLikeReply(token: String, id: Int) : Single<retrofit2.Response<Response<Any>>>
    fun deleteReply(token: String, id: Int) : Single<retrofit2.Response<Response<Any>>>
    fun addReply(token: String, reply: ReplyRequest) : Single<retrofit2.Response<Response<Any>>>
}

class PostServiceImpl(private val api: PostApi) : PostService {

    override fun addReply(token: String, reply: ReplyRequest) = api.addReply(token, reply)

    override fun deleteReply(token: String, id: Int) = api.deleteReply(token, "/api/post/reply/$id/")

    override fun likeReply(token: String, id: Int) = api.likeReply(token, "/api/post/like_reply/$id/")

    override fun unLikeReply(token: String, id: Int) = api.unLikeReply(token, "/api/post/unlike_reply/$id/")

    override fun unLikeComment(token: String, id: Int): Single<retrofit2.Response<Response<Any>>> = api.unLikeComment(token, "/api/post/unlike_comment/$id/")

    override fun likeComment(token: String, id: Int): Single<retrofit2.Response<Response<Any>>> = api.likeComment(token, "/api/post/like_comment/$id/")

    override fun getReply(token: String, id: Int, page: Int) = api.getReply(token, "/api/post/reply_list/$id/$page/")

    override fun getLikePost(token: String, id: Int) = api.getLike(token, PostId(id))

    override fun deleteComment(token: String, id: Int) = api.deleteComment(token, "/api/post/comment/$id/")

    override fun unlikePost(token: String, id: Int) = api.unlikePost(token, "/api/post/unlike_post/$id/")

    override fun likePost(token: String, id: Int) = api.likePost(token, "/api/post/like_post/$id/")

    override fun addComment(token: String, comment: CommentRequest) = api.addComment(token, comment)

    override fun getPostDetail(token: String, id: Int, page: Int) = api.getPostDetail(token, "/api/post/post/$id/$page/")

    override fun deletePost(token : String, id: Int): Single<retrofit2.Response<Response<Any>>> = api.deletePost(token,"/api/post/post/$id/0/")

    override fun addPost(token: String, text: String, userName: String, file: ArrayList<MultipartBody.Part>) =
        api.addPost(token, file, RequestBody.create(MediaType.parse("inputText/plain"), text), RequestBody.create(MediaType.parse("inputText/plain"), userName))

    override fun getPost(token : String, follower: Follower, page : Int) = api.getPost(token, GetPostRequest(page, follower.user_id))

}