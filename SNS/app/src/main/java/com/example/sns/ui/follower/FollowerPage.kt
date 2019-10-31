package com.example.sns.ui.follower

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

import com.example.sns.R
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentFollowerPageBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FollowerPage : BaseFragment<FragmentFollowerPageBinding, FollowerActivityViewModel>() {

    override val layoutResourceId = R.layout.fragment_follower_page

    override val viewModel: FollowerActivityViewModel by viewModel()

    override fun initView() {

    }

    override fun initObserver() {

    }

    override fun initListener() {

    }

    override fun initViewModel() {

    }

}
