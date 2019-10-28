package com.example.sns.ui.userInfo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sns.BR
import com.example.sns.R
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentPageUserInfoBinding
import com.example.sns.ui.main.MainActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserInfoPage : BaseFragment<FragmentPageUserInfoBinding, UserInfoViewModel>(),
    SwipeRefreshLayout.OnRefreshListener {


    override val layoutResourceId = R.layout.fragment_page_user_info

    override val viewModel: UserInfoViewModel by viewModel()

    override fun initView() {

    }

    override fun initObserver() {

    }

    override fun initListener() {

    }

    override fun initViewModel() {

    }

    override fun onRefresh() {

    }

}