package com.sun.unsplash_02.screen.search

import android.view.View
import com.sun.unsplash_02.R
import com.sun.unsplash_02.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment() {

    override fun getLayoutResourceId() = R.layout.fragment_search

    override fun onViewCreated(view: View) {
    }

    override fun onInit() {
        toolbarSearch.setNavigationIcon(R.drawable.ic_arrow_back)
        super.onInit()
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}
