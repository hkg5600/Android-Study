package com.example.sns.ui.post

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
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

open class PostPage : BaseFragment<FragmentPagePostBinding, PostViewModel>(),
    SwipeRefreshLayout.OnRefreshListener {

    val ADD_POST = 1

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
                }
                is UserInfo -> {
                    UserObject.userInfo?.value = it
                }
            }
        })

        UserObject.userInfo.let {
            it?.observe(this, Observer {
                refreshPostList()
            })
        }

        viewModel.error.observe(this, Observer {
            if (it == "failed to connect") {
                makeToast("네트워크 신호가 약합니다")
                viewModel.getUser()
            }
        })



    }

    override fun initListener() {
        viewDataBinding.buttonAdd.setOnClickListener {
            startActivityForResult(Intent(activity, AddPostActivity::class.java), ADD_POST)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK)
            refreshPostList()
    }

    private fun refreshPostList() {
        viewModel.getPost(Follower(UserObject.userInfo?.value?.followers!!))

    }
}