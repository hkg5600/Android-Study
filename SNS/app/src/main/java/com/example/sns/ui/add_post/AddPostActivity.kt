package com.example.sns.ui.add_post

import android.app.Activity
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityAddPostBinding
import com.example.sns.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddPostActivity : BaseActivity<ActivityAddPostBinding, AddPostViewModel>() {


    override val layoutResourceId = R.layout.activity_add_post

    override val viewModel: AddPostViewModel by viewModel()

    override fun initView() {

        viewDataBinding.viewModel = viewModel
    }

    override fun initObserver() {
        viewModel.message.observe(this, Observer {
            makeToast(it)
            when (it) {
                "게시물 저장 성공" -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        })
    }

    override fun initViewModel() {

    }

    override fun initListener() {

    }


}
