package com.sun.unsplash_02.base

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sun.unsplash_02.R

abstract class BaseActivity : AppCompatActivity() {

    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResourceId())
        ProgressDialog(this).apply {
            setMessage(getString(R.string.msg_loading))
            setCancelable(false)
            isIndeterminate = true
            progressDialog = this
        }
        initView()
        initData()
    }

    abstract fun getLayoutResourceId(): Int
    abstract fun initView()
    abstract fun initData()

    fun showProgressDialog() {
        progressDialog?.let {
            if (!it.isShowing) it.show()
        }
    }

    fun dismissProgressDialog() {
        progressDialog?.let {
            if (it.isShowing) it.dismiss()
        }
    }
}
