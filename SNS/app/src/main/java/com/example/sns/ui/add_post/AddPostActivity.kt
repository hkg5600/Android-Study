package com.example.sns.ui.add_post

import android.app.Activity
import android.net.Uri
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.adapter.GalleryImageAdapter
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
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout


class AddPostActivity : BaseActivity<ActivityAddPostBinding, AddPostViewModel>() {

    override val layoutResourceId = R.layout.activity_add_post

    override val viewModel: AddPostViewModel by viewModel()

    private val imageAdapter: GalleryImageAdapter by inject()
    override fun initView() {
        title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        viewDataBinding.recyclerView.run {
            layoutManager = GridLayoutManager(context, 3)
            setHasFixedSize(true)
            adapter = imageAdapter
        }
        viewDataBinding.slidingPanel.openPane()
        viewDataBinding.viewModel = viewModel
    }

    override fun initObserver() {
        viewModel.message.observe(this, Observer {
            makeToast(it, false)
            when (it) {
                "게시물 저장 성공" -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        })

        viewModel.error.observe(this, Observer {
            when (it) {
                "failed to connect" -> {
                    finish()
                    makeToast(resources.getString(R.string.network_error), false)
                }
            }
        })

        imageAdapter.overSize.observe(this, Observer {
            makeToast("최대 13개까지 선택 가능합니다", false)
        })

        imageAdapter.onChanged.observe(this, Observer {
            invalidateOptionsMenu()
        })

    }

    override fun initViewModel() {
        imageAdapter.setImage(goToAlbum())
    }

    override fun initListener() {
        viewDataBinding.text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                invalidateOptionsMenu()
            }

        })

        viewDataBinding.text.onFocusChangeListener =
            View.OnFocusChangeListener { p0, p1 ->
                if (!p1) {
                    hideKeyboard(p0)
                    viewDataBinding.text.clearFocus()
                }
            }


        viewDataBinding.slidingPanel.setPanelSlideListener(object :
            SlidingUpPanelLayout.PanelSlideListener,
            SlidingPaneLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View, slideOffset: Float) {
                hideKeyboard()
            }

            override fun onPanelClosed(panel: View) {
                viewDataBinding.text.clearFocus()
            }

            override fun onPanelOpened(panel: View) {
                showKeyboard()
                viewDataBinding.text.requestFocus()
            }

            override fun onPanelStateChanged(
                panel: View?,
                previousState: SlidingUpPanelLayout.PanelState?,
                newState: SlidingUpPanelLayout.PanelState?
            ) {

            }


        })
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun goToAlbum() = viewModel.getImageFromGallery(this)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                makeToast("취소되었습니다", false)
                finish()
            }

            R.id.save_post -> {
                if (imageAdapter.selectedImageList.value?.isNotEmpty()!!) {
                    loadFile()
                    viewModel.checkNetwork()
                } else {
                    makeToast("최소 한장의 사진을 선택해주세요", false)
                    viewDataBinding.slidingPanel.closePane()
                }
            }

            R.id.clear_image -> {
                imageAdapter.clearValue()
                makeToast("모두 선택 해제했습니다", false)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun loadFile() {
        if (imageAdapter.selectedImageList.value?.isNotEmpty()!!) {
            imageAdapter.selectedImageList.value.run {
                this?.forEach {
                    val filePath =
                        FileManager.getRealPathFromURI(Uri.parse(it.uri), applicationContext)
                    val file = File(filePath!!)
                    if (file.exists()) {
                        val requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), file)
                        val multipartData =
                            MultipartBody.Part.createFormData(
                                "image_${this.indexOf(it)}",
                                "file.jpg",
                                requestFile
                            )
                        viewModel.file.add(multipartData)
                    }
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_post, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.getItem(0)?.isEnabled = imageAdapter.selectedImageList.value?.isNotEmpty()!!
        menu?.getItem(1)?.isEnabled = viewDataBinding.text.text.isNotEmpty()
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if (!viewDataBinding.slidingPanel.isOpen) {
            viewDataBinding.slidingPanel.openPane()
        } else {
            super.onBackPressed()
        }
    }
}
