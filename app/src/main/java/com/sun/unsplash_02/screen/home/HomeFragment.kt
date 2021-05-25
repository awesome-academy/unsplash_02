package com.sun.unsplash_02.screen.home

import android.view.View
import com.sun.unsplash_02.R
import com.sun.unsplash_02.base.BaseFragment

class HomeFragment : BaseFragment() {

    override fun getLayoutResourceId() = R.layout.fragment_home

    override fun onViewCreated(view: View) {
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
