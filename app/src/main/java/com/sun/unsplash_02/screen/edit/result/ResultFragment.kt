package com.sun.unsplash_02.screen.edit.result

import android.net.Uri
import android.view.View
import androidx.core.os.bundleOf
import com.sun.unsplash_02.R
import com.sun.unsplash_02.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_result.*

class ResultFragment : BaseFragment() {

    override fun getLayoutResourceId() = R.layout.fragment_result

    override fun initView(view: View) {
    }

    override fun initData() {
        arguments?.let {
            val imageUri = it.getString(EXTRA_RESULT_IMAGE)
            imageResult.setImageURI(Uri.parse(imageUri))
        }
    }

    companion object {
        private const val EXTRA_RESULT_IMAGE = "EXTRA_RESULT_IMAGE"

        fun newInstance(imageUri: String) = ResultFragment().apply {
            arguments = bundleOf(EXTRA_RESULT_IMAGE to imageUri)
        }
    }
}
