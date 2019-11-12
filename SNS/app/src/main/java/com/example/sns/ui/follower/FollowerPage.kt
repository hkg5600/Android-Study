package com.example.sns.ui.follower

import com.example.sns.R
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentFollowerPageBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FollowerPage : BaseFragment<FragmentFollowerPageBinding, FollowerViewModel>() {

    override val layoutResourceId = R.layout.fragment_follower_page

    override val viewModel: FollowerViewModel by viewModel()

    override fun initView() {

    }

    override fun initObserver() {

    }

    override fun initListener() {

    }

    override fun initViewModel() {

    }

}
