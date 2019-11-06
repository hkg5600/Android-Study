package com.example.sns.utils

import android.app.AlertDialog
import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.custom_dialog.view.*


class CustomDialog(val context: Context) {
    private lateinit var dialog: View
    private lateinit var showDialog: AlertDialog
    inner class Builder {
        private lateinit var builder: AlertDialog.Builder

        fun setView(initDialog: View): Builder {
            dialog = initDialog
            builder = AlertDialog.Builder(context).setView(dialog)
            return this
        }

        fun setMessage(msg: String): Builder {
            dialog.text_view.text = msg
            return this
        }

        fun show() {
            showDialog =  builder.show()
        }
    }

    inner class Listener {

        fun setOkayButton(onClickListener: View.OnClickListener): Listener {
            dialog.text_ok.setOnClickListener(onClickListener)
            return this
        }

        fun setCancelButton(onClickListener: View.OnClickListener) {
            dialog.text_cancel.setOnClickListener(onClickListener)
        }
    }
    fun dismiss() {
        showDialog.dismiss()
    }
}
