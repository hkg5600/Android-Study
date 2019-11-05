package com.example.sns.ui.post

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sns.R
import com.example.sns.adapter.PostAdapter
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentPagePostBinding
import com.example.sns.network.model.Follower
import com.example.sns.network.model.Post
import com.example.sns.network.model.UserInfo
import com.example.sns.network.response.PostList
import com.example.sns.ui.add_post.AddPostActivity
import com.example.sns.utils.UserObject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module.applicationContext

open class PostPage : BaseFragment<FragmentPagePostBinding, PostViewModel>(),
    SwipeRefreshLayout.OnRefreshListener {

    override val layoutResourceId = R.layout.fragment_page_post

    override val viewModel: PostViewModel by viewModel()

    private val postAdapter: PostAdapter by inject()


    override fun initView() {
        viewDataBinding.recyclerView.setHasFixedSize(true)
        viewDataBinding.recyclerView.adapter = postAdapter
        viewDataBinding.swipeRefreshLayout.setOnRefreshListener(this)
    }

    override fun initObserver() {

        viewModel.data.observe(this, Observer {
            viewDataBinding.swipeRefreshLayout.isRefreshing = false
            when (it) {
                is PostList -> {
                    postAdapter.setPost(it.post)
                    viewDataBinding.recyclerView.scrollToPosition(0)
                }
                is UserInfo -> {
                    UserObject.userInfo.value = it
                }
            }
        })

        viewModel.message.observe(this, Observer {
            makeToast(it)
            when(it) {
                "게시물 삭제 성공" -> refreshPostList()
            }
        })

        UserObject.userInfo.observe(this, Observer {
            refreshPostList()
        })

        viewModel.error.observe(this, Observer {
            if (it == "failed to connect") {
                makeToast(resources.getString(R.string.network_error))
                viewModel.getUser()
            }
        })


    }

    override fun initListener() {
        postAdapter.onItemClickListener = object : PostAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int, holder: PostAdapter.PostHolder) {
                val p = PopupMenu(context, view)
                if (UserObject.userInfo.value?.user_id == postAdapter.postList[position].owner) {
                    activity?.menuInflater?.inflate(R.menu.menu_my_post_option, p.menu)
                    p.apply {
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.delete_post -> {
                                    val builder = AlertDialog.Builder(context)
                                    val dialogView =layoutInflater.inflate(R.layout.custom_dialog, null )
                                    builder.setView(dialogView).show()
                                }
                                R.id.edit_post -> {
                                    makeToast("수정하기")
                                }
                                R.id.turn_off -> {
                                    makeToast("알림 해제")
                                }
                            }
                            false
                        }
                        show()
                    }
                } else {
                    activity?.menuInflater?.inflate(R.menu.menu_others_post_option, p.menu)
                    p.apply {
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.un_follow -> {
                                    makeToast("팔로우 취소")
                                }
                                R.id.share_post -> {
                                    makeToast("공유하기")
                                }
                                R.id.hide_post -> {
                                    makeToast("숨기기")
                                }
                            }
                            false
                        }
                        show()
                    }
                }
            }
        }


    }

    override fun initViewModel() {
        viewDataBinding.swipeRefreshLayout.isRefreshing = true
        viewModel.getUser()
    }

    var swipeLayout: SwipeRefreshLayout? = null

    override fun onRefresh() {
        viewDataBinding.swipeRefreshLayout.isRefreshing = true
        refreshPostList()
    }

    private fun refreshPostList() {
        viewModel.getPost(Follower(UserObject.userInfo.value?.followers!!))
    }
}