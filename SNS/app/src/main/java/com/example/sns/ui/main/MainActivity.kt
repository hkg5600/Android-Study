package com.example.sns.ui.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.adapter.PagerAdapter
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_main

    override val viewModel: MainActivityViewModel by viewModel()

    override fun initStartView() {


    }

    override fun initDataBinding() {
        viewModel.success.observe(this, Observer {
            Log.d("Success", "success to insert user")
            val mPagerAdapter = PagerAdapter(supportFragmentManager)
            val pager = viewDataBinding.viewPager
            val tabs : TabLayout = viewDataBinding.tabs
            pager.adapter = mPagerAdapter
            tabs.setupWithViewPager(pager)
            Log.d("Msg", "finish main activity")
        })
    }

    override fun initAfterBinding() {
        Log.d("Msg", "get token")
        Log.d("Msg", "in MainActivity")
    }

}
