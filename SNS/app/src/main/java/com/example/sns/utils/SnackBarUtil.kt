package com.example.sns.utils

import android.view.Gravity
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar

object SnackBarUtil {

    lateinit var snackBar: Snackbar

    fun getSnackBar(viewBelow: View, view: View, msg: String, clickMsg: String, onClick: () -> Unit) {
        snackBar = Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE).apply {
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
}