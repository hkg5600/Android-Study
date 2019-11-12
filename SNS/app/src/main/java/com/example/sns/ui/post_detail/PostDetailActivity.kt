package com.example.sns.ui.post_detail

import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sns.R
import com.example.sns.adapter.CommentAdapter
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityPostDetailBinding
import com.example.sns.network.response.PostDetail
import com.example.sns.utils.BASE_URL
import com.example.sns.utils.DateTimeConverter
import com.example.sns.utils.UserObject
import kotlinx.android.synthetic.main.app_bar.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostDetailActivity : BaseActivity<ActivityPostDetailBinding, PostDetailActivityViewModel>(),
    SwipeRefreshLayout.OnRefreshListener {

    override val layoutResourceId: Int = R.layout.activity_post_detail

    override val viewModel: PostDetailActivityViewModel by viewModel()

    private val commentAdapter: CommentAdapter by inject()

    override fun initView() {
        window.setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE)
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (intent.getBooleanExtra("edit", false)) {
            showKeyboard()
            viewDataBinding.editTextComment.requestFocus()
        }

        viewDataBinding.recyclerViewComment.setHasFixedSize(true)
        viewDataBinding.recyclerViewComment.adapter = commentAdapter
        viewDataBinding.swipeRefreshLayout.setOnRefreshListener(this)
    }

    override fun initObserver() {
        viewModel.data.observe(this, Observer {
            viewDataBinding.swipeRefreshLayout.isRefreshing = false
            when (it) {
                is PostDetail -> {
                    viewModel.owner = it.owner
                    viewModel.post.set(it.copy(it.id, it.text, it.owner, DateTimeConverter.jsonTimeToTime(it.created_at), it.like, it.profile_image))
                    Glide.with(applicationContext).load(BASE_URL + it.profile_image.profile_image)
                        .apply(RequestOptions.circleCropTransform()).into(viewDataBinding.ownerProfileImage)
                    commentAdapter.setCommentLIst(it.comments)
                }
            }
        })

        viewModel.error.observe(this, Observer {
            viewDataBinding.swipeRefreshLayout.isRefreshing = false
            when (it) {
                "게시물이 없습니다" -> finish()
            }
        })

        viewModel.message.observe(this, Observer {
            makeToast(it, false)
            when (it) {
                "댓글 작성 성공" -> {
                    viewDataBinding.editTextComment.text.clear()
                    viewDataBinding.editTextComment.clearFocus()
                    hideKeyboard()
                    viewModel.getPostDetail()
                }
            }
        })


    }

    override fun initListener() {
        viewDataBinding.editTextComment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewDataBinding.btnAnswerApply.isEnabled =
                    viewDataBinding.editTextComment.text.toString().isNotEmpty()
            }
        })

        commentAdapter.onLongClickListener = object : CommentAdapter.OnItemLongClickListener {
            override fun onClick(
                view: View,
                position: Int,
                holder: CommentAdapter.CommentHolder
            ): Boolean {
                val p = PopupMenu(applicationContext, view)
                if (UserObject.userInfo?.user?.user_id == commentAdapter.commentList[position].owner)
                    menuInflater.inflate(R.menu.menu_my_comment, p.menu)
                else
                    menuInflater.inflate(R.menu.menu_comment, p.menu)
                p.apply {
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.delete_comment -> {
                                showDialog(
                                    "삭제하시겠습니까?",
                                    {
                                        viewModel.deleteComment(commentAdapter.commentList[position].id)
                                        commentAdapter.commentList.remove(commentAdapter.commentList[position])
                                        commentAdapter.notifyDataSetChanged()
                                    },
                                    { makeToast("취소", false) })
                            }
                            R.id.menu_report -> {
                                showDialog(
                                    "신고하시겠습니까?",
                                    {},
                                    { makeToast("취소", false) }
                                )
                            }
                        }
                        false
                    }
                    show()
                }
                return true
            }
        }
    }

    override fun initViewModel() {
        viewDataBinding.vm = viewModel

        if (intent.hasExtra("id")) {
            viewDataBinding.swipeRefreshLayout.isRefreshing = true
            viewModel.id = intent.getIntExtra("id", -1)
            viewModel.getPostDetail()
        } else {
            finish()
        }
    }

    override fun onRefresh() {
        viewModel.getPostDetail()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //makeToast("취소되었습니다", false)
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
