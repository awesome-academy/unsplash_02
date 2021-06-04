package com.sun.unsplash_02.screen.main

import com.sun.unsplash_02.R
import com.sun.unsplash_02.base.BaseActivity
import com.sun.unsplash_02.screen.home.HomeFragment
import com.sun.unsplash_02.utils.showAlertDialog

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
}
