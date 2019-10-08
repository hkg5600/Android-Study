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
import com.example.sns.ui.login.LoginActivity
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
            val tabs: TabLayout = viewDataBinding.tabs
            pager.adapter = mPagerAdapter
            tabs.setupWithViewPager(pager)
            Log.d("Msg", "finish main activity")
        })

        viewModel.logoutSuccess.observe(this, Observer {
            if (it) {
                startActivity(Intent(this, LoginActivity::class.java))
                Toast.makeText(applicationContext, "success to log out", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("Error", "while log out")
                Toast.makeText(applicationContext, "failed to log out", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun initAfterBinding() {
        Log.d("Msg", "get token")
        Log.d("Msg", "in MainActivity")
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
}
