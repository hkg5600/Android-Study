package com.example.sns.ui.add_post

import OnSwipeTouchListener
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityAddPostBinding
import com.example.sns.ui.main.MainActivity
import com.example.sns.utils.FileManager
import kotlinx.android.synthetic.main.app_bar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class AddPostActivity : BaseActivity<ActivityAddPostBinding, AddPostViewModel>() {

    private val PICK_FROM_ALBUM = 1

    override val layoutResourceId = R.layout.activity_add_post

    override val viewModel: AddPostViewModel by viewModel()


    override fun initView() {
        title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        viewDataBinding.viewModel = viewModel
    }

    override fun initObserver() {
        viewModel.message.observe(this, Observer {
            makeToast(it)
            when (it) {
                "게시물 저장 성공" -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        })

    }

    override fun initViewModel() {

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initListener() {
        viewDataBinding.holderLayout.setOnTouchListener(object : OnSwipeTouchListener() {
            override fun onSwipeLeft() {
                val intent = Intent(Intent.ACTION_PICK).apply {
                    type = "image/*"
                }
                startActivityForResult(intent, PICK_FROM_ALBUM)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                makeToast("취소되었습니다")
                finish()
            }

            R.id.save_post -> {
                viewModel.checkNetwork()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FROM_ALBUM && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            val filePath = FileManager.getRealPathFromURI(uri!!, applicationContext)
            val file = File(filePath)
            if (file.exists()) {
                Log.d("Path", "$filePath")
                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                val multipartData = MultipartBody.Part.createFormData("image", file.name, requestFile)
                viewModel.file = multipartData
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_post, menu)
        return true
    }

}
