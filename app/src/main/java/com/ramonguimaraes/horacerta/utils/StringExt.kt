package com.ramonguimaraes.horacerta.utils

fun String?.isValid(): Boolean {
    return this != null && this.isNotEmpty() && this.isNotBlank()
}
