package com.ramonguimaraes.horacerta.utils

import android.util.Patterns

fun String?.isValid(): Boolean {
    return this != null && this.isNotEmpty() && this.isNotBlank()
}

fun String?.isEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this ?: "").matches()
}
