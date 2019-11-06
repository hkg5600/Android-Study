package com.example.sns.ui.login

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityLoginBinding
import com.example.sns.network.model.LoginData
import com.example.sns.network.model.UserInfo
import com.example.sns.ui.main.MainActivity
import com.example.sns.utils.TokenObject
//import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginActivityViewModel>() {

    override val layoutResourceId = R.layout.activity_login
    override val viewModel: LoginActivityViewModel by viewModel()

    override fun initView() {
        viewDataBinding.vm = viewModel
    }

    override fun initObserver() {

        viewModel.data.observe(this, Observer {
            when (it) {
                is LoginData -> {
                    TokenObject.token = "Token ${it.token}"
                    viewModel.insertToken(TokenObject.token)
                }
            }
        })

        viewModel.roomSuccess.observe(this, Observer {
            when (it) {
                "token" -> startActivity(Intent(this, MainActivity::class.java))
            }
        })

        viewModel.error.observe(this, Observer {
            when (it) {
                "failed to connect" -> makeToast(resources.getString(R.string.network_error), false)
                "error" -> makeToast("아이디 또는 비밀번호가 일치하지 않습니다", false)
            }
        })

    }

    override fun initViewModel() {

    }

    override fun initListener() {
        viewDataBinding.editTextId.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewDataBinding.loginBtn.isEnabled = viewDataBinding.editTextId.text.toString().isNotEmpty() && viewDataBinding.editTextPw.text.toString().isNotEmpty()
            }

        })

        viewDataBinding.editTextPw.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewDataBinding.loginBtn.isEnabled = viewDataBinding.editTextId.text.toString().isNotEmpty() && viewDataBinding.editTextPw.text.toString().isNotEmpty()
            }

        })

        viewDataBinding.editTextPw.setOnEditorActionListener  { v, actionId, event ->
            return@setOnEditorActionListener  when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    if (viewDataBinding.loginBtn.isEnabled)
                        viewModel.login()
                    true
                }
                else -> false
            }
        }

        viewDataBinding.editTextId.setOnEditorActionListener { view, actionId, event ->
            return@setOnEditorActionListener  when (actionId) {
                EditorInfo.IME_ACTION_NEXT -> {
                    viewDataBinding.editTextPw.requestFocus()
                    true
                }
                else -> false
            }
        }
    }


}
