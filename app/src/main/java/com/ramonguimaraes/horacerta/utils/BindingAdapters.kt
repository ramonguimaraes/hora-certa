package com.ramonguimaraes.horacerta.utils

import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter

@BindingAdapter("visibility")
fun setVisibility(view: View, boolean: Boolean) {
    view.visibility = if (boolean) View.VISIBLE else View.GONE
}

@BindingAdapter("android:text")
fun setDoubleText(view: EditText, value: Double) {
    view.setText(value.toString())
}

@InverseBindingAdapter(attribute = "android:text")
fun getDoubleText(view: EditText): Double {
    return try {
        view.text.toString().toDouble()
    } catch (e: NumberFormatException) {
        0.0
    }
}

@BindingAdapter("android:text")
fun setLongText(view: EditText, value: Long) {
    view.setText(value.toString())
}

@InverseBindingAdapter(attribute = "android:text")
fun getLongText(view: EditText): Long {
    return try {
        view.text.toString().toLong()
    } catch (e: NumberFormatException) {
        0
    }
}
