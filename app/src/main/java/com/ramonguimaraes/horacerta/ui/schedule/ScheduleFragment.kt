package com.ramonguimaraes.horacerta.ui.schedule

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.FragmentScheduleBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScheduleFragment : Fragment() {

    private val mBinding: FragmentScheduleBinding by lazy {
        FragmentScheduleBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding.txtDate.setOnClickListener {
            val year = Calendar.getInstance().get(Calendar.YEAR)
            val month = Calendar.getInstance().get(Calendar.MONTH)
            val dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->

                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, dayOfMonth)

                    val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
                        Calendar.SUNDAY -> "Domingo"
                        Calendar.MONDAY -> "Segunda"
                        Calendar.TUESDAY -> "Terça"
                        Calendar.WEDNESDAY -> "Quarta"
                        Calendar.THURSDAY -> "Quinta"
                        Calendar.FRIDAY -> "Sexta"
                        else -> "Sábado"
                    }

                    val format = SimpleDateFormat("MMM", Locale.forLanguageTag("PT-BR"))
                    val formattedMonth = format.format(calendar.time)
                        .subSequence(0, 3)
                        .toString()
                        .replaceFirstChar { it.uppercase() }

                    val formattedDate = resources.getString(
                        R.string.date_temaplate,
                        dayOfWeek,
                        dayOfMonth,
                        formattedMonth,
                        year
                    )
                    mBinding.txtDate.text = formattedDate
                },
                year, month, dayOfMonth
            ).show()
        }

        val recyclerView = mBinding.rvSchedule
        recyclerView.adapter = ScheduleAdapter(
            listOf(
                Appointment("08:00 - 09:00", "Cliente 1", "Corte de cabelo"),
                Appointment("09:00 - 10:00", "Cliente 2", "Corte de cabelo"),
                Appointment("10:00 - 11:00", "Cliente 3", "Corte de cabelo"),
                Appointment("11:00 - 12:00", "Cliente 4", "Corte de cabelo"),
                Appointment("12:00 - 13:00", "Cliente 5", "Corte de cabelo"),
                Appointment("12:00 - 13:00", "Cliente 5", "Corte de cabelo"),
                Appointment("12:00 - 13:00", "Cliente 5", "Corte de cabelo"),
                Appointment("12:00 - 13:00", "Cliente 5", "Corte de cabelo"),
                Appointment("12:00 - 13:00", "Cliente 5", "Corte de cabelo"),
                Appointment("12:00 - 13:00", "Cliente 5", "Corte de cabelo"),
                Appointment("12:00 - 13:00", "Cliente 5", "Corte de cabelo"),
                Appointment("12:00 - 13:00", "Cliente 5", "Corte de cabelo"),
                Appointment("12:00 - 13:00", "Cliente 5", "Corte de cabelo"),
                Appointment("12:00 - 13:00", "Cliente 5", "Corte de cabelo"),
            )
        )
        recyclerView.layoutManager = LinearLayoutManager(context)
        return mBinding.root
    }
}