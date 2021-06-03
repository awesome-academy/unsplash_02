package com.sun.unsplash_02.screen.detail

import android.view.View
import androidx.core.os.bundleOf
import com.sun.unsplash_02.R
import com.sun.unsplash_02.base.BaseFragment
import com.sun.unsplash_02.data.model.Image
import com.sun.unsplash_02.utils.LoadImageTask
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_detail.view.*

class DetailFragment : BaseFragment() {

    override fun getLayoutResourceId() = R.layout.fragment_detail

    override fun initView(view: View) {
        view.toolbarDetail.run {
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                activity?.supportFragmentManager?.popBackStack()
            }
        }
    }

    override fun initData() {
        val image = arguments?.getParcelable<Image>(ARGUMENT_IMAGE)
        LoadImageTask(imageDetail).execute(image?.urls?.full)
    }

    companion object {
        private const val ARGUMENT_IMAGE = "ARGUMENT_IMAGE"

        fun newInstance(image: Image) = DetailFragment().apply {
            arguments = bundleOf(ARGUMENT_IMAGE to image)
        }
    }
}
