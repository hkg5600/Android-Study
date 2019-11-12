package com.example.sns.ui.post_detail

import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sns.R
import com.example.sns.adapter.LikeListAdapter
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityPostLikeBinding
import com.example.sns.network.model.PostLike
import com.example.sns.network.response.PostLikeList
import com.example.sns.network.response.PostList
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class PostLikeActivity : BaseActivity<ActivityPostLikeBinding, PostLikeActivityViewModel>() ,
    SwipeRefreshLayout.OnRefreshListener  {


    override val layoutResourceId = R.layout.activity_post_like

    override val viewModel: PostLikeActivityViewModel by viewModel()
    private val likeAdapter : LikeListAdapter by inject()

    override fun initView() {
        viewDataBinding.recyclerView.setHasFixedSize(true)
        viewDataBinding.recyclerView.adapter = likeAdapter
        viewDataBinding.swipeRefreshLayout.setOnRefreshListener(this)
    }

    override fun initObserver() {
        viewModel.data.observe(this, Observer {
            when(it) {
                is PostLikeList -> likeAdapter.setUserList(it.user_list)
            }
        })
    }

    override fun initListener() {

    }

    override fun initViewModel() {
        viewModel.getLike(intent.getIntExtra("id", -1))
    }

    override fun onRefresh() {

    }
}
