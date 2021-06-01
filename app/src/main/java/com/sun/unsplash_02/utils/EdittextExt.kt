package com.sun.unsplash_02.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun EditText.showKeyboard(activity: Activity) {
    val inputMethodManager =
        activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    requestFocus()
    inputMethodManager.showSoftInput(this, 0)
}

fun EditText.hideKeyboard(activity: Activity) {
    val inputMethodManager =
        activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    clearFocus()
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}

fun EditText.moveCursorToEnd() {
    setSelection(this.text.toString().length)
}
