package com.ramonguimaraes.horacerta.utils

import android.content.res.Resources
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.google.android.material.textfield.TextInputLayout

@InverseBindingAdapter(attribute = "app:getText")
fun getText(editText: EditText): String? {
    return editText.text?.toString()
}

@BindingAdapter("app:getText")
fun setText(editText: EditText, value: String?) {
    editText.setText(value)
}

@BindingAdapter("app:error")
fun setError(view: TextInputLayout, error: String?) {
    error?.let {
        view.error = it
    }
}
