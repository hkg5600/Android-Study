package com.example.sns.ui.add_post

import OnSwipeTouchListener
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.sns.R
import com.example.sns.adapter.ImageAdapter
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityAddPostBinding
import com.example.sns.utils.FileManager
import kotlinx.android.synthetic.main.app_bar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.android.ext.android.inject
import java.io.File

class AddPostActivity : BaseActivity<ActivityAddPostBinding, AddPostViewModel>() {

    private val PICK_FROM_ALBUM = 1

    override val layoutResourceId = R.layout.activity_add_post

    override val viewModel: AddPostViewModel by viewModel()

    private val imageAdapter: ImageAdapter by inject()
    override fun initView() {
        title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        viewDataBinding.recyclerView.run {
            layoutManager = StaggeredGridLayoutManager(3, 1).apply {
                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
                orientation = StaggeredGridLayoutManager.VERTICAL
            }
            setHasFixedSize(true)
            adapter = imageAdapter
        }
        viewDataBinding.slidingPanel.openPane()
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

        imageAdapter.overSize.observe(this, Observer {
            makeToast("최대 13개까지 선택 가능합니다")
        })

    }

    override fun initViewModel() {
        imageAdapter.setImage(goToAlbum())
    }



    override fun initListener() {

    }

    private fun goToAlbum() = viewModel.getImageFromGallery(this)

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
                val multipartData =
                    MultipartBody.Part.createFormData("image", "file.jpg", requestFile)
                viewModel.file = multipartData
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_post, menu)
        return true
    }

    override fun onBackPressed() {
        if (viewDataBinding.slidingPanel.isOpen) {
            viewDataBinding.slidingPanel.closePane()
        } else {
            super.onBackPressed()
        }
    }
}
