package com.example.sns.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

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

    fun makeToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}