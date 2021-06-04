package com.sun.unsplash_02.screen.edit.crop

import android.view.View
import com.sun.unsplash_02.R
import com.sun.unsplash_02.base.BaseFragment
import com.sun.unsplash_02.screen.edit.CompleteEditListener

class CropFragment : BaseFragment(), CompleteEditListener {

    override fun getLayoutResourceId() = R.layout.fragment_crop

    override fun initView(view: View) {
    }

    override fun initData() {
    }

    override fun onComplete() {
    }

    companion object {
        fun newInstance() = CropFragment()
    }
}
