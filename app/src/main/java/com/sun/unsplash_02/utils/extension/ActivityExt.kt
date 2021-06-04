package com.sun.unsplash_02.utils.extension

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.sun.unsplash_02.R

fun Activity.showAlertDialog(msg: String, onConfirm: () -> Unit) {
    AlertDialog.Builder(this)
        .setTitle(R.string.app_name)
        .setMessage(msg)
        .setNegativeButton(android.R.string.no, null)
        .setPositiveButton(android.R.string.yes) { _, _ -> onConfirm() }
        .create()
        .show()
}
