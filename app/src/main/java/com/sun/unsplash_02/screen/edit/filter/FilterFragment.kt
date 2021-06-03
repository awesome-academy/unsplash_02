package com.sun.unsplash_02.screen.edit.filter

import android.view.View
import com.sun.unsplash_02.R
import com.sun.unsplash_02.base.BaseFragment

class FilterFragment : BaseFragment() {

    override fun getLayoutResourceId() = R.layout.fragment_filter

    override fun initView(view: View) {
    }

    override fun initData() {
    }

    companion object {
        fun newInstance() = FilterFragment()
    }
}
