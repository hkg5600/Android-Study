package com.example.sns.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.sns.utils.CustomDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar


abstract class BaseActivity<T : ViewDataBinding, R : BaseViewModel> : AppCompatActivity() {

    lateinit var viewDataBinding: T

    abstract val layoutResourceId: Int

    abstract val viewModel: R

    abstract fun initView()

    abstract fun initObserver()

    abstract fun initListener()

    abstract fun initViewModel()

    private var isSetBackButtonValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, layoutResourceId)
        viewDataBinding.executePendingBindings()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        initView()
        initObserver()
        initListener()
        initViewModel()
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(if (currentFocus == null) View(this) else currentFocus!!)
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Fragment.showKeyboard() {
        view?.let { activity?.showKeyboard(it) }
    }

    fun Activity.showKeyboard() {
        showKeyboard(if (currentFocus == null) View(this) else currentFocus!!)
    }

    private fun Context.showKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
    }

    fun makeToast(msg: String, isLong: Boolean) {
        if (isLong)
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        else
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun showDialog(msg: String, ok: () -> Unit, cancel: () -> Unit) {
        CustomDialog(this).run {
            Builder()
                .setView(
                    LayoutInflater.from(context).inflate(
                        com.example.sns.R.layout.custom_dialog,
                        null
                    )
                )
                .setMessage(msg)
                .show()
            Listener()
                .setOkayButton(View.OnClickListener {
                    ok()
                    this.dismiss()
                })
                .setCancelButton(View.OnClickListener {
                    cancel()
                    this.dismiss()
                })
        }
    }

    fun getSnackBar(viewBelow : View ,view: View, msg: String, clickMsg: String, onClick: () -> Unit) =
        Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE).apply {
            val snackBarView = this.view
            val params = snackBarView.layoutParams as CoordinatorLayout.LayoutParams
            params.anchorId = viewBelow.id
            params.anchorGravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            snackBarView.layoutParams = params
            setAction(clickMsg) {
                onClick()
                dismiss()
            }
        }
}