package com.sun.unsplash_02.screen.edit.draw

import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import com.sun.unsplash_02.R
import com.sun.unsplash_02.base.BaseFragment
import com.sun.unsplash_02.screen.edit.CompleteEditListener
import com.sun.unsplash_02.screen.edit.result.ResultFragment
import com.sun.unsplash_02.utils.FileUtils
import com.sun.unsplash_02.utils.extension.addFragment
import com.sun.unsplash_02.utils.extension.getBitmapFromView
import kotlinx.android.synthetic.main.fragment_draw.*

class DrawFragment : BaseFragment(), CompleteEditListener {

    override fun getLayoutResourceId() = R.layout.fragment_draw

    override fun initView(view: View) {
    }

    override fun initData() {
        arguments?.let {
            val imageUri = it.getString(ARGUMENT_IMAGE)
            photoDraw.setImageDraw(Uri.parse(imageUri))
        }
    }

    override fun onComplete() {
        photoDraw.getBitmapFromView()?.let {
            addFragment(
                R.id.frameEditContainer, ResultFragment.newInstance(
                    FileUtils.saveBitmap(
                        requireContext(),
                        it,
                        System.currentTimeMillis().toString()
                    ).toString()
                )
            )
        } ?: run {
            Toast.makeText(
                requireContext(),
                getString(R.string.can_not_save_image),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        private const val ARGUMENT_IMAGE = "ARGUMENT_IMAGE"

        fun newInstance(imageUri: String) = DrawFragment().apply {
            arguments = bundleOf(ARGUMENT_IMAGE to imageUri)
        }
    }
}
