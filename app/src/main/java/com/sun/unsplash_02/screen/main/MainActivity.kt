package com.sun.unsplash_02.screen.main

import androidx.appcompat.app.AlertDialog
import com.sun.unsplash_02.R
import com.sun.unsplash_02.base.BaseActivity
import com.sun.unsplash_02.screen.home.HomeFragment

class MainActivity : BaseActivity() {

    override fun getLayoutResourceId() = R.layout.activity_main

    override fun initView() {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(HomeFragment::javaClass.name)
            .replace(R.id.frameMainContainer, HomeFragment.newInstance())
            .commit()
    }

    override fun initData() {
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            when (supportFragmentManager.findFragmentById(R.id.frameMainContainer)) {
                is HomeFragment -> {
                    showAlertDialog(getString(R.string.msg_exit_app)) {
                        finish()
                    }
                }
                else -> {
                    super.onBackPressed()
                }
            }
        } else {
            super.onBackPressed()
        }
    }

    private fun showAlertDialog(msg: String, onConfirm: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(R.string.app_name)
            .setMessage(msg)
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes) { _, _ -> onConfirm() }
            .create()
            .show()
    }
}
