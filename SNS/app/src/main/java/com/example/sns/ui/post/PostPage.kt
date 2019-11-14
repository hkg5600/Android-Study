package com.example.sns.ui.post

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sns.R
import com.example.sns.adapter.PostAdapter
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentPagePostBinding
import com.example.sns.network.model.Follower
import com.example.sns.network.response.UserInfo
import com.example.sns.network.response.PostList
import com.example.sns.ui.login.LoginActivity
import com.example.sns.ui.post_detail.PostDetailActivity
import com.example.sns.ui.post_detail.PostLikeActivity
import com.example.sns.utils.UserObject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

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

        postAdapter.clickUserNameText.observe(this, Observer {
            makeToast(it, false)
        })

        viewModel.roomSuccess.observe(this, Observer {
            when (it) {
                "delete Token" -> {
                    startActivity(Intent(context, LoginActivity::class.java))
                    makeToast("로그아웃 성공", false)
                }
                else -> makeToast("로그아웃에 실패했습니다", false)
            }
        })

        viewModel.data.observe(this, Observer {
            viewDataBinding.swipeRefreshLayout.isRefreshing = false
            when (it) {
                is PostList -> {
                    postAdapter.setPost(it.post)
                    postAdapter.nextPage = it.nextPage
                    postAdapter.lastPage = it.last_page
                }
                is UserInfo -> {
                    postAdapter.userName = it.user.user_id
                    UserObject.userInfo = it
                    refreshPost()
                }
            }
        })

        viewModel.message.observe(this, Observer {
            makeToast(it, false)
            when (it) {
                "게시물 삭제 성공" -> loadPost()
            }
        })

        viewModel.error.observe(this, Observer {
            when (it) {
                "failed to connect" -> {
                    makeToast(resources.getString(R.string.network_error), false)
                    viewModel.getUser()
                }
                "Forbidden" -> {
                    makeToast("세션이 만료되었습니다", false)
                    viewModel.logout()
                }
            }
        })

        postAdapter.loadMore.observe(this, Observer {
            loadPost()
        })
    }

    override fun initListener() {

        postAdapter.onShowLikeClickListener = object : PostAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int, holder: PostAdapter.PostHolder) {
                startActivity(Intent(context, PostLikeActivity::class.java).putExtra("id", postAdapter.postList[position].id))
            }

        }

        postAdapter.onCommentBtnClickListener = object : PostAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int, holder: PostAdapter.PostHolder) {
                startActivity(Intent(context, PostDetailActivity::class.java).putExtra("id", postAdapter.postList[position].id).putExtra("edit", true))
            }

        }

        postAdapter.onShowDetailClickListener = object : PostAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int, holder: PostAdapter.PostHolder) {
                if (holder.showDetail) {
                    startActivity(Intent(context, PostDetailActivity::class.java).putExtra("id", postAdapter.postList[position].id).putExtra("edit", false))
                } else {
                    postAdapter.clearSelectedItem()
                    postAdapter.selectedItem.put(position, true)
                    val params = holder.textViewText.layoutParams.apply {
                        height = ViewGroup.LayoutParams.WRAP_CONTENT
                        width = 0
                    }
                    holder.textViewText.layoutParams = params
                    holder.btnShowFullText.visibility = View.GONE
                    holder.showDetail = true
                    postAdapter.notifyDataSetChanged()
                }
            }

        }

        postAdapter.onLikeClickListener = object : PostAdapter.OnItemClickListener {
            @SuppressLint("SetTextI18n")
            override fun onClick(view: View, position: Int, holder: PostAdapter.PostHolder) {
                postAdapter.postList[position].run {
                    if (like.contains(UserObject.userInfo?.user?.user_id)) {
                        viewModel.unlikePost(id)
                        like.remove(UserObject.userInfo?.user?.user_id)
                        holder.btnLike.setImageResource(R.drawable.ic_unlike)
                    } else {
                        viewModel.likePost(id)
                        like.add(UserObject.userInfo?.user?.user_id!!)
                        holder.btnLike.setImageResource(R.drawable.ic_like)
                    }
                    if (like.isNotEmpty()) holder.likeCount.text = "${like[0]}님 외 ${like.size-1}명이 좋아합니다"
                    else holder.likeCount.text = "첫 좋아요를 눌러주세요"
                }

            }

        }

        postAdapter.onItemClickListener = object : PostAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int, holder: PostAdapter.PostHolder) {
                val p = PopupMenu(context, view)
                if (UserObject.userInfo?.user?.user_id == postAdapter.postList[position].owner)
                    activity?.menuInflater?.inflate(R.menu.menu_my_post_option, p.menu)
                else
                    activity?.menuInflater?.inflate(R.menu.menu_others_post_option, p.menu)
                p.apply {
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.delete_post -> {
                                showDialog(
                                    "삭제하시겠습니까?",
                                    { viewModel.deletePost(postAdapter.postList[position].id)
                                    postAdapter.postList.remove(postAdapter.postList[position])
                                    postAdapter.notifyDataSetChanged()},
                                    { makeToast("취소", false) })
                            }
                            R.id.edit_post -> {
                                makeToast("수정하기", false)
                            }
                            R.id.turn_off -> {
                                makeToast("알림 해제", false)
                            }
                            R.id.un_follow -> {
                                makeToast("팔로우 취소", false)
                            }
                            R.id.share_post -> {
                                makeToast("공유하기", false)
                            }
                            R.id.hide_post -> {
                                makeToast("숨기기", false)
                            }
                        }
                        false
                    }
                    show()
                }
            }
        }
    }

    override fun initViewModel() {
        if (UserObject.userInfo == null)
            viewModel.getUser()
    }

    var swipeLayout: SwipeRefreshLayout? = null

    override fun onRefresh() {
        refreshPost()
    }

    private fun loadPost() {
        viewDataBinding.swipeRefreshLayout.isRefreshing = true
        UserObject.userInfo?.let {
            viewModel.getPost(getFollowings(it), postAdapter.nextPage)
        }
    }

    fun refreshPost() {
        postAdapter.postList.clear()
        Log.e("Refreshing", "isRefreshing")
        viewDataBinding.swipeRefreshLayout.isRefreshing = true
        UserObject.userInfo?.let {
            viewModel.getPost(getFollowings(it), 0)
        }
    }


    private fun getFollowings(userInfo: UserInfo) : Follower = with(userInfo.following) {
        Follower(this.map {
            it.following_user_id
        })
    }
}