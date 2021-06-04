package com.sun.unsplash_02.utils.extension

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.sun.unsplash_02.base.BaseActivity

fun Fragment.addFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    tag: String = fragment::class.java.simpleName
) {
    activity?.supportFragmentManager?.apply {
        beginTransaction()
            .addToBackStack(tag)
            .add(containerId, fragment, tag)
            .commit()
    }
}

fun Fragment.showProgressDialog() {
    if (activity is BaseActivity) {
        (activity as BaseActivity).showProgressDialog()
    }
}

fun Fragment.hideProgressDialog() {
    if (activity is BaseActivity) {
        (activity as BaseActivity).dismissProgressDialog()
    }
}
