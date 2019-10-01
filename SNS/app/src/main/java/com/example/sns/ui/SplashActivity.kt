package com.example.sns.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sns.ui.login.LoginActivity
import com.example.sns.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}