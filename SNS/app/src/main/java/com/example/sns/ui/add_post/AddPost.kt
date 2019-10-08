package com.example.sns.ui.add_post

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

class AddPost : BaseActivity<ActivityAddPostBinding, AddPostViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.activity_add_post

    override val viewModel: AddPostViewModel by viewModel()

    override fun initStartView() {

        viewDataBinding.button.setOnClickListener {
            viewModel.addPost(
                viewDataBinding.title.text.toString(),
                viewDataBinding.text.text.toString()
            )
        }
    }

    override fun initDataBinding() {
        viewModel.success.observe(this, Observer {
            Toast.makeText(this, "Success to add post", Toast.LENGTH_SHORT)
            startActivity(Intent(this, MainActivity::class.java))
        })
    }

    override fun initAfterBinding() {

    }




}
