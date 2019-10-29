package com.example.sns.ui.add_post

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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

    private var permission: Boolean = false


    override fun initView() {
        checkPermission()

        setSupportActionBar(toolbar)
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

    override fun initListener() {
        viewDataBinding.buttonImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            startActivityForResult(intent, PICK_FROM_ALBUM)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FROM_ALBUM && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            val filePath = FileManager.getRealPathFromURI(uri!!, applicationContext)
            val file = File(filePath)
            if (file.exists()) {
                Log.d("Path", "$filePath")
                val requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file)
                val multipartData =
                    MultipartBody.Part.createFormData("image", file.name, requestFile)
                viewModel.file = multipartData
            }
        }
    }

}
