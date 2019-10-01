package com.example.sns.ui.userInfo

import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sns.R
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentPageUserInfoBinding
import com.example.sns.ui.main.MainActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserInfoPage : BaseFragment<FragmentPageUserInfoBinding, MainActivityViewModel>() ,
    SwipeRefreshLayout.OnRefreshListener {

    override val layoutResourceId: Int
        get() = R.layout.fragment_page_user_info

    override val viewModel: MainActivityViewModel by viewModel()

    fun newInstance(): UserInfoPage {
        val args = Bundle()
        val fragment = UserInfoPage()
        fragment.arguments = args
        return fragment
    }

    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }

    override fun onRefresh() {

    }

}