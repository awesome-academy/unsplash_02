package com.sun.unsplash_02.screen.edit.emoji

import android.view.View
import com.sun.unsplash_02.R
import com.sun.unsplash_02.base.BaseFragment

class EmojiFragment : BaseFragment() {

    override fun getLayoutResourceId() = R.layout.fragment_emoji

    override fun initView(view: View) {
    }

    override fun initData() {
    }

    companion object {
        fun newInstance() = EmojiFragment()
    }
}
