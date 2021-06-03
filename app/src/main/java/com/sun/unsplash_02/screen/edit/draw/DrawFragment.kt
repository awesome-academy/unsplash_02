package com.sun.unsplash_02.screen.edit.draw

import android.view.View
import com.sun.unsplash_02.R
import com.sun.unsplash_02.base.BaseFragment

class DrawFragment : BaseFragment() {

    override fun getLayoutResourceId() = R.layout.fragment_draw

    override fun initView(view: View) {
    }

    override fun initData() {
    }

    companion object {
        fun newInstance() = DrawFragment()
    }
}
