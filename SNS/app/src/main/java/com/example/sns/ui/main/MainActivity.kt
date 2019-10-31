package com.example.sns.ui.main

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityMainBinding
import com.example.sns.network.model.UserInfo
import com.example.sns.ui.follower.FollowerPage
import com.example.sns.ui.login.LoginActivity
import com.example.sns.ui.post.PostPage
import com.example.sns.ui.userInfo.UserInfoPage
import com.example.sns.utils.UserObject
import kotlinx.android.synthetic.main.app_bar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    override val layoutResourceId = R.layout.activity_main

    override val viewModel: MainActivityViewModel by viewModel()

    private val postPage = PostPage()
    private val userInfoPage = UserInfoPage()
    private val followerPage = FollowerPage()

    override fun initView() {
        setSupportActionBar(toolbar)
        initNavigation()
    }

    override fun initObserver() {

        viewModel.roomSuccess.observe(this, Observer {
            when (it) {
                "delete Token" -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    makeToast("로그아웃 성공")
                }
                else -> makeToast("로그아웃에 실패했습니다")
            }
        })
    }

    override fun initListener() {

    }

    override fun initViewModel() {

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


    private fun initNavigation() {
        var transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, postPage).commitAllowingStateLoss()

        viewDataBinding.bottomNavigationView.setOnNavigationItemSelectedListener{
            transaction = supportFragmentManager.beginTransaction()
            when(it.itemId) {
                R.id.menu_post -> transaction.replace(R.id.frame_layout, postPage).commitAllowingStateLoss()
                R.id.menu_my_info -> transaction.replace(R.id.frame_layout, userInfoPage).commitAllowingStateLoss()
                R.id.menu_follower -> transaction.replace(R.id.frame_layout, followerPage).commitAllowingStateLoss()
            }
            true
        }
    }
}
