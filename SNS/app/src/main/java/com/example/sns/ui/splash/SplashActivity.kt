package com.example.sns.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.sns.ui.login.LoginActivity
import com.example.sns.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    val viewModel: SplashActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.token.observe(this, Observer {
            when(it) {
                else -> startActivity(Intent(this, LoginActivity::class.java))
                //else -> startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        })
    }
}