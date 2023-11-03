package com.ramonguimaraes.horacerta.utils

import java.text.NumberFormat
import java.util.Locale

fun Double.toCurrency(locale: Locale = Locale("pt", "BR")): String {
    val formatter = NumberFormat.getCurrencyInstance(locale)
    return formatter.format(this)
}
