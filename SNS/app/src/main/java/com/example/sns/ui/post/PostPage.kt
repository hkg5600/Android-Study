package com.example.sns.ui.post

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sns.R
import com.example.sns.adapter.PostAdapter
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentPagePostBinding
import com.example.sns.network.model.Post
import com.example.sns.ui.add_post.AddPost
import com.example.sns.ui.main.MainActivityViewModel
import kotlinx.android.synthetic.main.fragment_page_post.*
import kotlinx.android.synthetic.main.fragment_page_post.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

open class PostPage : BaseFragment<FragmentPagePostBinding, PostViewModel>(),
    SwipeRefreshLayout.OnRefreshListener {

    override val layoutResourceId: Int
        get() = R.layout.fragment_page_post

    override val viewModel: PostViewModel by viewModel()

    private val postAdapter: PostAdapter by inject()


    override fun initStartView() {
        viewDataBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        viewDataBinding.recyclerView.setHasFixedSize(true)
        viewDataBinding.recyclerView.adapter = postAdapter
        viewDataBinding.swipeRefreshLayout.setOnRefreshListener(this)
    }

    override fun initDataBinding() {
        Log.d("Msg", "in PostPage")
        viewModel.success.observe(this, Observer {
            Log.d("Succeess", "liveData In")
            it.let {
                postAdapter.setPost(viewModel.liveData.value!!)
                Log.d("Data", "${viewModel.liveData.value}")
            }
        })

        viewModel.user.observe(this, Observer {
            viewModel.getPost(viewModel.user.value?.followers?.user_id!!)
        })

        viewDataBinding.buttonAdd.setOnClickListener {
            startActivity(Intent(activity, AddPost::class.java))
        }
    }

    override fun initAfterBinding() {

    }

    var swipeLayout: SwipeRefreshLayout? = null

    fun newInstance(): PostPage {
        Log.d("Msg", "new Instance")
        val args = Bundle()
        val fragment = PostPage()
        fragment.arguments = args
        return fragment
    }

    override fun onRefresh() {
        Log.d("Msg", "Refresh")
        viewModel.getPost(viewModel.user.value?.followers?.user_id!!)
        viewDataBinding.swipeRefreshLayout.isRefreshing = false
    }
}