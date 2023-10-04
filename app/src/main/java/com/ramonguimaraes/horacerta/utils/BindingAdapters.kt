package com.ramonguimaraes.horacerta.utils

import android.net.Uri
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.ramonguimaraes.horacerta.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@BindingAdapter("visibility")
fun setVisibility(view: View, boolean: Boolean) {
    view.visibility = if (boolean) View.VISIBLE else View.GONE
}

@BindingAdapter("android:text")
fun setDoubleText(view: EditText, value: Double) {
    view.setText(value.toString())
}

@BindingAdapter("android:text")
fun setDateText(view: TextView, value: Calendar) {
    runCatching {
        val dayOfWeek = when (value.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "Domingo"
            Calendar.MONDAY -> "Segunda"
            Calendar.TUESDAY -> "Terça"
            Calendar.WEDNESDAY -> "Quarta"
            Calendar.THURSDAY -> "Quinta"
            Calendar.FRIDAY -> "Sexta"
            else -> "Sábado"
        }

        val format = SimpleDateFormat("MMM", Locale.forLanguageTag("PT-BR"))
        val formattedMonth = format.format(value.time)
            .subSequence(0, 3)
            .toString()
            .replaceFirstChar { it.uppercase() }

        val formattedDate = view.context.resources.getString(
            R.string.date_temaplate,
            dayOfWeek,
            value.get(Calendar.DAY_OF_MONTH),
            formattedMonth,
            value.get(Calendar.YEAR)
        )

        view.text = formattedDate
    }.onFailure {
        view.text = ""
    }
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

@BindingAdapter("android:uri")
fun setUri(view: ImageView, uri: Uri?) {
    if (uri != null && uri != Uri.EMPTY) {
        Picasso.get()
            .load(uri)
            .placeholder(R.drawable.loading)
            .error(R.drawable.user_default)
            .into(view)
    }
}

