package com.example.sns.network.service

import com.example.sns.network.Response
import com.example.sns.network.api.PostApi
import com.example.sns.network.model.Follower
import com.example.sns.network.request.CommentId
import com.example.sns.network.request.CommentRequest
import com.example.sns.network.request.GetPostRequest
import com.example.sns.network.request.PostId
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
    fun getPostDetail(token: String, id:Int) : Single<retrofit2.Response<Response<PostDetail>>>
    fun addComment(token: String, comment: CommentRequest) : Single<retrofit2.Response<Response<Any>>>
    fun likePost(token: String, post: Int) : Single<retrofit2.Response<Response<Any>>>
    fun unlikePost(token: String, post: Int) : Single<retrofit2.Response<Response<Any>>>
    fun deleteComment(token: String, id: Int) : Single<retrofit2.Response<Response<Any>>>
    fun getLike(token: String, id: Int) : Single<retrofit2.Response<Response<PostLikeList>>>
    fun getReply(token: String, id: Int) : Single<retrofit2.Response<Response<ReplyList>>>
}

class PostServiceImpl(private val api: PostApi) : PostService {

    override fun getReply(token: String, id: Int) = api.getReply(token, CommentId(id))

    override fun getLike(token: String, id: Int) = api.getLike(token, PostId(id))

    override fun deleteComment(token: String, id: Int) = api.deleteComment(token, "/api/post/comment/$id/")

    override fun unlikePost(token: String, post: Int) = api.unlikePost(token, PostId(post))

    override fun likePost(token: String, post: Int) = api.likePost(token, PostId(post))

    override fun addComment(token: String, comment: CommentRequest) = api.addComment(token, comment)

    override fun getPostDetail(token: String, id: Int) = api.getPostDetail(token, "/api/post/post/$id/")

    override fun deletePost(token : String, id: Int): Single<retrofit2.Response<Response<Any>>> = api.deletePost(token,"/api/post/post/$id/")

    override fun addPost(token: String, text: String, userName: String, file: ArrayList<MultipartBody.Part>) =
        api.addPost(token, file, RequestBody.create(MediaType.parse("text/plain"), text), RequestBody.create(MediaType.parse("text/plain"), userName))

    override fun getPost(token : String, follower: Follower, page : Int) = api.getPost(token, GetPostRequest(page, follower.user_id))

}