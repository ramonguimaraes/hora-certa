package com.ramonguimaraes.horacerta.utils

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibility")
fun setVisibility(view: View, boolean: Boolean) {
    view.visibility = if (boolean) View.VISIBLE else View.GONE
}
