package com.example.sns.ui.post_detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Editable
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
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
import com.example.sns.network.response.ReplyList
import com.example.sns.utils.BASE_URL
import com.example.sns.utils.DateTimeConverter
import com.example.sns.utils.SnackBarUtil
import com.example.sns.utils.UserObject
import kotlinx.android.synthetic.main.app_bar.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostDetailActivity : BaseActivity<ActivityPostDetailBinding, PostDetailActivityViewModel>(),
    SwipeRefreshLayout.OnRefreshListener {

    override val layoutResourceId: Int = R.layout.activity_post_detail

    override val viewModel: PostDetailActivityViewModel by viewModel()

    private val commentAdapter: CommentAdapter by inject()
    lateinit var commentHolder: CommentAdapter.CommentHolder

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

    @SuppressLint("SetTextI18n")
    override fun initObserver() {

        commentAdapter.deleteReply.observe(this, Observer {
            showDialog(
                "삭제하시겠습니까?",
                {
                    viewModel.deleteReply(it)
                },
                { makeToast("취소", false) })
        })
        commentAdapter.reportReply.observe(this, Observer {
            showDialog(
                "신고하시겠습니까?",
                {

                },
                { makeToast("취소", false) })
        })

        commentAdapter.likeToReply.observe(this, Observer {
            viewModel.likeReply(it)
        })

        commentAdapter.unLikeToReply.observe(this, Observer {
            viewModel.unLikeReply(it)
        })

        commentAdapter.clickUserNameText.observe(this, Observer {
            makeToast(it, false)
        })

        viewModel.data.observe(this, Observer {
            viewDataBinding.swipeRefreshLayout.isRefreshing = false
            when (it) {
                is PostDetail -> {
                    viewModel.post.set(it.also {
                        it.post.created_at = DateTimeConverter.jsonTimeToTime(it.post.created_at)
                    })
                    viewDataBinding.textViewPost.movementMethod = LinkMovementMethod.getInstance()
                    viewModel.setText(it.post.text, getClickableSpan(it), 0, it.post.owner.length)
                    Glide.with(applicationContext)
                        .load(BASE_URL + it.post.profile_image.profile_image)
                        .apply(RequestOptions.circleCropTransform())
                        .into(viewDataBinding.ownerProfileImage)
                    commentAdapter.setCommentLIst(it.comment)
                    commentAdapter.nextPage = it.nextPage
                    commentAdapter.lastPage = it.last_page
                }

                is ReplyList -> {
                    if (it.reply.isNotEmpty()) {
                        commentHolder.nextCount = it.next_count
                        commentHolder.nextReply = it.nextPage
                        commentHolder.showReply.text = "이전 답글 보기 (${it.next_count}개)"
                        commentHolder.replyAdapter.setReplyLIst(it.reply)
                    }
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
                    refreshComment()
                }
                "답글 작성 성공" -> {
                    SnackBarUtil.snackBar.dismiss()
                    viewDataBinding.editTextComment.text.clear()
                    viewDataBinding.editTextComment.clearFocus()
                    hideKeyboard()
                    refreshComment()
                }
                "답글 삭제 성공" -> {
                    refreshComment()
                }
            }
        })

        commentAdapter.loadMore.observe(this, Observer {
            loadComment()
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

        commentAdapter.onLikeButtonClickListener = object : CommentAdapter.OnItemClickListener {
            @SuppressLint("SetTextI18n")
            override fun onClick(view: View, position: Int, holder: CommentAdapter.CommentHolder) {
                commentAdapter.commentList[position].run {
                    if (like.contains(UserObject.userInfo?.user?.user_id)) {
                        viewModel.unLikeComment(id)
                        like.remove(UserObject.userInfo?.user?.user_id)
                        holder.likeComment.setImageResource(R.drawable.ic_unlike)
                    } else {
                        viewModel.likeComment(id)
                        like.add(UserObject.userInfo?.user?.user_id!!)
                        holder.likeComment.setImageResource(R.drawable.ic_like)
                    }
                    holder.likeCount.text = "좋아요 ${commentAdapter.commentList[position].like.size}개"
                }
            }
        }

        commentAdapter.onEditReplyClickListener = object : CommentAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int, holder: CommentAdapter.CommentHolder) {
                commentHolder = holder
                SnackBarUtil.getSnackBar(viewDataBinding.editTextComment, viewDataBinding.holderLayout, "${commentAdapter.commentList[position].owner}님에게 답글을 작성중..", "취소") {
                    viewModel.isReplyAdd = false
                    hideKeyboard()
                    viewDataBinding.editTextComment.clearFocus()
                }
                SnackBarUtil.snackBar.show()

                viewModel.commentId = commentAdapter.commentList[position].id
                viewModel.isReplyAdd = true
                showKeyboard()
                viewDataBinding.editTextComment.requestFocus()
                showReply(position, holder)
            }
        }

        commentAdapter.onShowReplyClickListener = object : CommentAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int, holder: CommentAdapter.CommentHolder) {
                showReply(position, holder)
            }
        }

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
        if (intent.hasExtra("postId")) {
            viewDataBinding.swipeRefreshLayout.isRefreshing = true
            viewModel.postId = intent.getIntExtra("postId", -1)
            refreshComment()
        } else {
            finish()
        }
    }

    override fun onRefresh() {
        refreshComment()
    }

    private fun refreshComment() {
        commentAdapter.clearSelectedItem()
        commentAdapter.commentList.clear()
        viewModel.getPostDetail(intent.getIntExtra("postId", -1), 0)
    }


    private fun loadComment() {
        viewModel.getPostDetail(intent.getIntExtra("postId", -1), commentAdapter.nextPage)
    }

    @SuppressLint("SetTextI18n")
    private fun showReply(position: Int, holder: CommentAdapter.CommentHolder) {
        if (holder.showReply.text == "답글 보기 (${commentAdapter.commentList[position].reply_count}개)") {
            holder.replyAdapter.replyList.clear()
            viewModel.getReply(commentAdapter.commentList[position].id, 0)
        } else
            viewModel.getReply(commentAdapter.commentList[position].id, holder.nextReply)
        commentAdapter.selectedItem.put(position, true)
        holder.recyclerView.visibility = View.VISIBLE
        commentHolder = holder
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getClickableSpan(it: PostDetail) = object : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
            ds.color = Color.DKGRAY
            ds.isUnderlineText = false
        }

        override fun onClick(p0: View) {
            makeToast(it.post.owner, false)
        }
    }

}
