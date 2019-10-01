package com.example.sns.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.sns.ui.post.PostPage
import com.example.sns.ui.userInfo.UserInfoPage

class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment =
        when(position) {
            0 -> PostPage().newInstance()
            1 -> UserInfoPage().newInstance()
            else -> PostPage().newInstance()
        }


    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? =
        when(position) {
            0 -> "Post"
            1 -> "My Info"
            else -> null
        }

}