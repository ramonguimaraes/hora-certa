package com.ramonguimaraes.horacerta.utils

import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

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
