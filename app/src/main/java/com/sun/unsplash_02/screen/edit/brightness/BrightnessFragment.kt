package com.sun.unsplash_02.screen.edit.brightness

import android.view.View
import com.sun.unsplash_02.R
import com.sun.unsplash_02.base.BaseFragment

class BrightnessFragment : BaseFragment() {

    override fun getLayoutResourceId() = R.layout.fragment_brightness

    override fun initView(view: View) {
    }

    override fun initData() {
    }

    companion object {
        fun newInstance() = BrightnessFragment()
    }
}
