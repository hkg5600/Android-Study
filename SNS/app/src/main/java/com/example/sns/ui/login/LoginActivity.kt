package com.example.sns.ui.login

import android.content.Intent
import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityLoginBinding
import com.example.sns.network.model.LoginData
import com.example.sns.network.model.UserInfo
import com.example.sns.ui.main.MainActivity
import com.example.sns.utils.TokenObject
//import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginActivityViewModel>() {

    override val layoutResourceId = R.layout.activity_login
    override val viewModel: LoginActivityViewModel by viewModel()

    override fun initView() {
        viewDataBinding.vm = viewModel
    }

    override fun initObserver() {

        viewModel.data.observe(this, Observer {
            when (it) {
                is LoginData -> {
                    TokenObject.token = it.token
                    viewModel.insertToken(TokenObject.token)
                }
            }
        })

        viewModel.roomSuccess.observe(this, Observer {
            when (it) {
                "token" -> startActivity(Intent(this, MainActivity::class.java))
            }
        })

    }

    override fun initViewModel() {

    }

    override fun initListener() {

    }


}
