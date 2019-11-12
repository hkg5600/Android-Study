package com.example.sns.ui.post_detail

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sns.R
import com.example.sns.adapter.LikeListAdapter
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityPostLikeBinding
import com.example.sns.network.response.PostLikeList
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.fragment_follower_page.edit_text_search
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class PostLikeActivity : BaseActivity<ActivityPostLikeBinding, PostLikeActivityViewModel>(),
    SwipeRefreshLayout.OnRefreshListener {


    override val layoutResourceId = R.layout.activity_post_like

    override val viewModel: PostLikeActivityViewModel by viewModel()
    private val likeAdapter: LikeListAdapter by inject()

    override fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        viewDataBinding.recyclerView.setHasFixedSize(true)
        viewDataBinding.recyclerView.adapter = likeAdapter
        viewDataBinding.swipeRefreshLayout.setOnRefreshListener(this)
        viewDataBinding.editTextSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0)
    }

    override fun initObserver() {
        viewModel.data.observe(this, Observer {
            when (it) {
                is PostLikeList -> likeAdapter.setUserList(it.user_list)
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initListener() {

        viewDataBinding.editTextSearch.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                if (edit_text_search.compoundDrawables[2] != null)
                    if (motionEvent.rawX >= edit_text_search.right - edit_text_search.compoundDrawables[2].bounds.width()) {
                        viewDataBinding.editTextSearch.text.clear()
                        true
                    }
            }
            false
        }

        viewDataBinding.editTextSearch.setOnEditorActionListener { textView, i, keyEvent ->
            return@setOnEditorActionListener  when (i) {
                EditorInfo.IME_ACTION_DONE -> {
                    hideKeyboard()
                    true
                }
                else -> false
            }
        }

        viewDataBinding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewDataBinding.editTextSearch.run {
                    if (text.isEmpty()) setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0)
                    else setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, R.drawable.ic_clear, 0)
                    likeAdapter.filterWithData(text.toString())
                }

            }

        })
    }

    override fun initViewModel() {
        viewModel.getLike(intent.getIntExtra("id", -1))
    }

    override fun onRefresh() {

    }
}
