package com.sun.unsplash_02.screen.edit

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.sun.unsplash_02.R
import com.sun.unsplash_02.base.BaseActivity
import com.sun.unsplash_02.data.model.TypeEdit
import com.sun.unsplash_02.screen.edit.brightness.BrightnessFragment
import com.sun.unsplash_02.screen.edit.crop.CropFragment
import com.sun.unsplash_02.screen.edit.draw.DrawFragment
import com.sun.unsplash_02.screen.edit.emoji.EmojiFragment
import com.sun.unsplash_02.screen.edit.filter.FilterFragment
import com.sun.unsplash_02.utils.extension.toGone
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : BaseActivity() {

    private var completeEditListener: CompleteEditListener? = null

    override fun getLayoutResourceId() = R.layout.activity_edit

    override fun initView() {
        toolbarEdit.run {
            setNavigationIcon(R.drawable.ic_close)
            setOnClickListener { finish() }
        }
        imageComplete.setOnClickListener {
            completeEditListener?.onComplete()
            toolbarEdit.title = getString(R.string.result)
            imageComplete.toGone()
        }
    }

    override fun initData() {
        intent?.let {
            val imageUrl = it.getStringExtra(EXTRA_IMAGE)
            when (it.getIntExtra(EXTRA_EDIT_TYPE, 0)) {
                TypeEdit.BRIGHTNESS.value -> {
                    toolbarEdit.title = getString(R.string.brightness)
                    imageUrl?.let { imageUri ->
                        replaceFragment(BrightnessFragment.newInstance(imageUri).also { fragment ->
                            completeEditListener = fragment
                        })
                    }
                }
                TypeEdit.CROP.value -> {
                    toolbarEdit.title = getString(R.string.crop)
                    imageUrl?.let {
                        replaceFragment(CropFragment.newInstance().also { fragment ->
                            completeEditListener = fragment
                        })
                    }
                }
                TypeEdit.DRAW.value -> {
                    toolbarEdit.title = getString(R.string.draw)
                    imageUrl?.let {
                        replaceFragment(DrawFragment.newInstance().also { fragment ->
                            completeEditListener = fragment
                        })
                    }
                }
                TypeEdit.FILTER.value -> {
                    toolbarEdit.title = getString(R.string.filter)
                    imageUrl?.let { imageUri ->
                        replaceFragment(FilterFragment.newInstance(imageUri).also { fragment ->
                            completeEditListener = fragment
                        })
                    }
                }
                TypeEdit.EMOJI.value -> {
                    toolbarEdit.title = getString(R.string.icon)
                    imageUrl?.let {
                        replaceFragment(EmojiFragment.newInstance().also { fragment ->
                            completeEditListener = fragment
                        })
                    }
                }
                else -> Unit
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().run {
            replace(R.id.frameEditContainer, fragment)
            addToBackStack(null)
            commit()
        }
    }

    companion object {
        private const val EXTRA_IMAGE = "EXTRA_IMAGE"
        private const val EXTRA_EDIT_TYPE = "EXTRA_EDIT_TYPE"

        fun getEditIntent(context: Context, imageUrl: String, type: Int) =
            Intent(context, EditActivity::class.java).apply {
                putExtra(EXTRA_IMAGE, imageUrl)
                putExtra(EXTRA_EDIT_TYPE, type)
            }
    }
}
