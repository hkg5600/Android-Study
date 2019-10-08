package com.example.sns.base

import android.database.DatabaseUtils
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sns.R
import com.example.sns.databinding.FragmentPagePostBinding
import com.example.sns.ui.post.PostPage

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel> : Fragment() {

    lateinit var viewDataBinding: T

    abstract val layoutResourceId: Int

    abstract val viewModel: V

    abstract fun initStartView()

    abstract fun initDataBinding()

    abstract fun initAfterBinding()

    private var isSetBackButtonValid = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewDataBinding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        viewDataBinding.executePendingBindings()

//BaseFragment
        initStartView()
        initDataBinding()
        initAfterBinding()

        return viewDataBinding.root
    }

}