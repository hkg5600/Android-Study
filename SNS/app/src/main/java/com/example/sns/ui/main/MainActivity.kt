package com.example.sns.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityMainBinding
import com.example.sns.ui.add_post.AddPostActivity
import com.example.sns.ui.follower.FollowerPage
import com.example.sns.ui.login.LoginActivity
import com.example.sns.ui.post.PostPage
import com.example.sns.ui.userInfo.UserInfoPage
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.app_bar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule
import kotlin.system.exitProcess

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {
    val ADD_POST = 1
    override val layoutResourceId = R.layout.activity_main

    override val viewModel: MainActivityViewModel by viewModel()

    private val postPage = PostPage()
    private val userInfoPage = UserInfoPage()
    private val followerPage = FollowerPage()

    private var permission: Boolean = false

    override fun initView() {
        setSupportActionBar(toolbar)
        initNavigation()
        checkPermission()
    }

    override fun initObserver() {

        viewModel.roomSuccess.observe(this, Observer {
            when (it) {
                "delete Token" -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    makeToast("로그아웃 성공", false)
                }
                else -> makeToast("로그아웃에 실패했습니다", false)
            }
        })

        viewModel.error.observe(this, Observer {

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

        viewDataBinding.bottomNavigationView.setOnNavigationItemSelectedListener {
            transaction = supportFragmentManager.beginTransaction()
            when (it.itemId) {
                R.id.menu_post -> {
                    transaction.replace(R.id.frame_layout, postPage).commitAllowingStateLoss()
                    postPage.loadPost()
                }
                R.id.menu_my_info -> transaction.replace(R.id.frame_layout, userInfoPage).commitAllowingStateLoss()
                R.id.menu_add_post -> startActivityForResult(Intent(this, AddPostActivity::class.java), ADD_POST)
                R.id.menu_follower -> transaction.replace(R.id.frame_layout, followerPage).commitAllowingStateLoss()
            }
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_POST) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, postPage).commitAllowingStateLoss()
            viewDataBinding.bottomNavigationView.selectedItemId = R.id.menu_post
            postPage.loadPost()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                (this),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            tedPermission()

        } else {
            permission = true
        }
    }

    private fun tedPermission() {

        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                permission = true
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                permission = false
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setRationaleMessage(getString(R.string.permissionMsg))
            .setDeniedMessage(getString(R.string.permissionDenied))
            .setPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .check()
    }

    private var mBackFlag = false
    override fun onBackPressed() {
        if (!mBackFlag) {
            makeToast("뒤로 버튼을 한번 더 누르면 종료됩니다", false)
            mBackFlag = true

            Timer("back", true).schedule(1500) {
                mBackFlag = false
                cancel()
            }
        } else {
            super.onBackPressed()
            onDestroy()
            exitProcess(0)
        }

    }
}
