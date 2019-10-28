package com.example.sns.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.adapter.PagerAdapter
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityMainBinding
import com.example.sns.network.model.Follower
import com.example.sns.network.model.UserInfo
import com.example.sns.ui.login.LoginActivity
import com.example.sns.utils.UserObject
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    override val layoutResourceId = R.layout.activity_main

    override val viewModel: MainActivityViewModel by viewModel()

    override fun initView() {


    }

    override fun initObserver() {

        viewModel.data.observe(this, Observer {
            when (it) {
                is UserInfo -> {
                    UserObject.userInfo = it
                    initPage()
                }
            }
        })

        viewModel.roomSuccess.observe(this, Observer {
            when (it) {
                "delete Token" -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    makeToast("success to log out")
                }
                else -> makeToast("failed to log out")
            }
        })
    }

    override fun initListener() {

    }

    override fun initViewModel() {
        viewModel.getUser()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_logout -> {
                viewModel.logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun initPage() {
        val mPagerAdapter = PagerAdapter(supportFragmentManager)
        val pager = viewDataBinding.viewPager
        val tabs: TabLayout = viewDataBinding.tabs
        pager.adapter = mPagerAdapter
        tabs.setupWithViewPager(pager)
    }
}
