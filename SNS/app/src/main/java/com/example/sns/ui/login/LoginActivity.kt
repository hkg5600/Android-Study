package com.example.sns.ui.login

import android.content.Intent
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityLoginBinding
import com.example.sns.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginActivityViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_login
    override val viewModel: LoginActivityViewModel by viewModel()

    override fun initStartView() {

    }

    override fun initDataBinding() {
        viewDataBinding.vm = viewModel

        viewModel.success.observe(this, Observer {
            Log.d("Success", "Good")
            startActivity(Intent(this, MainActivity::class.java))
        })
    }

    override fun initAfterBinding() {

    }


}
