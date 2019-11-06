package com.example.sns.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.sns.utils.CustomDialog

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel> : Fragment() {

    lateinit var viewDataBinding: T

    abstract val layoutResourceId: Int

    abstract val viewModel: V

    abstract fun initView()

    abstract fun initObserver()

    abstract fun initListener()

    abstract fun initViewModel()

    private var isSetBackButtonValid = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewDataBinding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        viewDataBinding.executePendingBindings()

//BaseFragment
        initView()
        initObserver()
        initListener()
        initViewModel()

        return viewDataBinding.root
    }

    fun makeToast(msg: String, isLong : Boolean) {
        if (isLong)
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        else
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showDialog(msg: String, ok: () -> Unit ,cancel: () -> Unit) {
        CustomDialog(context!!).run {
            Builder()
                .setView(LayoutInflater.from(context).inflate(com.example.sns.R.layout.custom_dialog, null))
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
}