package com.sun.unsplash_02.screen.edit.filter

import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.os.bundleOf
import com.sun.unsplash_02.R
import com.sun.unsplash_02.base.BaseFragment
import com.sun.unsplash_02.screen.edit.CompleteEditListener
import com.sun.unsplash_02.screen.edit.result.ResultFragment
import com.sun.unsplash_02.utils.FileUtils
import com.sun.unsplash_02.utils.ImageFilters
import com.sun.unsplash_02.utils.extension.addFragment
import com.sun.unsplash_02.utils.extension.getBitmapFromView
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.fragment_filter.view.*

class FilterFragment : BaseFragment(), CompleteEditListener, View.OnClickListener {

    private var oldBitmap: Bitmap? = null

    override fun getLayoutResourceId() = R.layout.fragment_filter

    override fun initView(view: View) {
        view.run {
            effectNone.setOnClickListener(this@FilterFragment)
            effectHighLight.setOnClickListener(this@FilterFragment)
            effectInvert.setOnClickListener(this@FilterFragment)
            effectGrayScale.setOnClickListener(this@FilterFragment)
        }
    }

    override fun initData() {
        arguments?.let {
            val imageUri = it.getString(FilterFragment.ARGUMENT_IMAGE)
            imageFilter.setImageURI(Uri.parse(imageUri))
        }
    }

    override fun onComplete() {
        imageFilter.getBitmapFromView()?.let {
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.effectHighLight -> {
                addFilter(oldBitmap, imageFilter) {
                    ImageFilters.applyHighlightEffect(it)
                }
            }
            R.id.effectInvert -> {
                addFilter(oldBitmap, imageFilter) {
                    ImageFilters.applyInvertEffect(it)
                }
            }
            R.id.effectGrayScale -> {
                addFilter(oldBitmap, imageFilter) {
                    ImageFilters.applyGreyscaleEffect(it)
                }
            }
            R.id.effectNone -> {
                oldBitmap?.let {
                    imageFilter.setImageBitmap(it)
                }
            }
        }
    }

    private fun addFilter(
        bitmap: Bitmap?,
        imageView: ImageView,
        filter: ((bmp: Bitmap) -> Bitmap?)
    ) {
        bitmap?.let {
            filter(it)?.let { bmp ->
                imageView.setImageBitmap(bmp)
            }
        } ?: run {
            oldBitmap = imageView.getBitmapFromView()?.also {
                filter(it)?.let { bmp ->
                    imageView.setImageBitmap(bmp)
                }
            }
        }
    }

    companion object {
        private const val ARGUMENT_IMAGE = "EXTRA_IMAGE"

        fun newInstance(imageUri: String) = FilterFragment().apply {
            arguments = bundleOf(ARGUMENT_IMAGE to imageUri)
        }
    }
}
