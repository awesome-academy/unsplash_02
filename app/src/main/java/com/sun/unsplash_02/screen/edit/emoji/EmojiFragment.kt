package com.sun.unsplash_02.screen.edit.emoji

import android.view.View
import com.sun.unsplash_02.R
import com.sun.unsplash_02.base.BaseFragment
import com.sun.unsplash_02.screen.edit.CompleteEditListener

class EmojiFragment : BaseFragment(), CompleteEditListener {

    override fun getLayoutResourceId() = R.layout.fragment_emoji

    override fun initView(view: View) {
    }

    override fun initData() {
    }

    override fun onComplete() {
    }

    companion object {
        fun newInstance() = EmojiFragment()
    }
}
