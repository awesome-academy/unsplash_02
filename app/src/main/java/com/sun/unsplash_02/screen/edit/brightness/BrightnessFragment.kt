package com.sun.unsplash_02.screen.edit.brightness

import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.os.bundleOf
import com.sun.unsplash_02.R
import com.sun.unsplash_02.base.BaseFragment
import com.sun.unsplash_02.screen.edit.CompleteEditListener
import com.sun.unsplash_02.screen.edit.result.ResultFragment
import com.sun.unsplash_02.utils.BrightnessThread
import com.sun.unsplash_02.utils.FileUtils
import com.sun.unsplash_02.utils.extension.addFragment
import com.sun.unsplash_02.utils.extension.getBitmapFromView
import kotlinx.android.synthetic.main.fragment_brightness.*
import kotlinx.android.synthetic.main.fragment_brightness.view.*

class BrightnessFragment : BaseFragment(), SeekBar.OnSeekBarChangeListener, CompleteEditListener {

    private var brightnessThread: BrightnessThread? = null

    override fun getLayoutResourceId() = R.layout.fragment_brightness

    override fun initView(view: View) {
        view.seekBarBrightness.setOnSeekBarChangeListener(this)
    }

    override fun initData() {
        arguments?.let {
            val imageUri = it.getString(ARGUMENT_IMAGE)
            imageBrightness.setImageURI(Uri.parse(imageUri))
            MediaStore.Images.Media.getBitmap(context?.contentResolver, Uri.parse(imageUri))
                ?.let { bmp ->
                    BrightnessThread(imageBrightness, bmp).run {
                        brightnessThread = this
                        start()
                    }
                }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        brightnessThread?.adjustBrightness(progress - 255)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

    override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit

    override fun onComplete() {
        imageBrightness.getBitmapFromView()?.let {
            addFragment(
                R.id.frameEditContainer, ResultFragment.newInstance(
                    FileUtils.saveBitmap(
                        requireContext(),
                        it,
                        System.currentTimeMillis().toString()
                    ).toString()
                )
            )
        } ?: kotlin.run {
            Toast.makeText(
                requireContext(),
                getString(R.string.can_not_save_image),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        private const val ARGUMENT_IMAGE = "EXTRA_IMAGE"

        fun newInstance(imageUri: String) = BrightnessFragment().apply {
            arguments = bundleOf(ARGUMENT_IMAGE to imageUri)
        }
    }
}
