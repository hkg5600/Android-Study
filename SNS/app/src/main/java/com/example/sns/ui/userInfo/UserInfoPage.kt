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

    override val layoutResourceId: Int
        get() = R.layout.fragment_page_user_info

    override val viewModel: UserInfoViewModel by viewModel()

    fun newInstance(): UserInfoPage {
        val args = Bundle()
        val fragment = UserInfoPage()
        fragment.arguments = args
        return fragment
    }

    override fun initStartView() {

    }

    override fun initDataBinding() {
        viewModel.user.observe(this, Observer {
            Log.d("Msg", "get user start")
            viewModel.getUser()
        })

        viewModel.success.observe(this, Observer {
            val test = viewModel.userInfo.get()?.userName!!
            Toast.makeText(activity, "$test", Toast.LENGTH_SHORT).show()
        })
    }

    override fun initAfterBinding() {

    }

    override fun onRefresh() {

    }

}