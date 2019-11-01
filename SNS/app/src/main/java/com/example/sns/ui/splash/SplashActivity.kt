package com.example.sns.ui.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.ui.login.LoginActivity
import com.example.sns.ui.main.MainActivity
import com.example.sns.utils.TokenObject
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.tokenDisposable()

        viewModel.success.observe(this, Observer {
            Log.d("Token", TokenObject.token)
            when(TokenObject.token) {
                "" -> startActivity(Intent(this, LoginActivity::class.java))
                else -> startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        })
    }


}