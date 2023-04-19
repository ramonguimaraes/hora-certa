package com.ramonguimaraes.horacerta.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}
