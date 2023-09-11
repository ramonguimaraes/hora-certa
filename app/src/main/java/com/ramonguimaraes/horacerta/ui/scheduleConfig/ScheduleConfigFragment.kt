package com.ramonguimaraes.horacerta.ui.scheduleConfig

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.FragmentScheduleConfigBinding
import com.ramonguimaraes.horacerta.domain.scheduleConfig.model.ScheduleConfig
import com.ramonguimaraes.horacerta.utils.DayOfWeek
import java.time.LocalTime

class ScheduleConfigFragment : Fragment() {
    val binding: FragmentScheduleConfigBinding by lazy {
        FragmentScheduleConfigBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val scheduleConfigList = listOf(
            ScheduleConfig(dayOfWeek = DayOfWeek.MONDAY),
            ScheduleConfig(dayOfWeek = DayOfWeek.TUESDAY),
            ScheduleConfig(dayOfWeek = DayOfWeek.WEDNESDAY),
            ScheduleConfig(dayOfWeek = DayOfWeek.THURSDAY),
            ScheduleConfig(dayOfWeek = DayOfWeek.FRIDAY),
            ScheduleConfig(dayOfWeek = DayOfWeek.SATURDAY),
            ScheduleConfig(dayOfWeek = DayOfWeek.SUNDAY)
        )

        val adapter = ScheduleConfigAdapter()
        adapter.submitList(scheduleConfigList)

        binding.rvScheduleConfig.layoutManager = LinearLayoutManager(context)
        binding.rvScheduleConfig.adapter = adapter

        binding.fabAddConfig.setOnClickListener {
            val bs = BottomSheetDialog(requireContext())
            bs.setContentView(R.layout.bottom_sheet_register_shedule_config_layout)
            val open = bs.findViewById<TextView>(R.id.txtOpenHour)
            val close = bs.findViewById<TextView>(R.id.txtIntervalStart)
            val reabre = bs.findViewById<TextView>(R.id.txtIntervalEnd)
            val fecha = bs.findViewById<TextView>(R.id.txtCloseHour)
            val save = bs.findViewById<Button>(R.id.btnSave)

            open?.setOnClickListener {
                TimePickerDialog(
                    requireContext(),
                    { timePicker, i, i2 ->
                        open.text = LocalTime.of(i, i2, 0).toString()
                    },
                    0,
                    0,
                    true
                ).show()
            }
            close?.setOnClickListener {
                TimePickerDialog(
                    requireContext(),
                    { timePicker, i, i2 ->
                        close.text = LocalTime.of(i, i2, 0).toString()
                    },
                    0,
                    0,
                    true
                ).show()
            }
            reabre?.setOnClickListener {
                TimePickerDialog(
                    requireContext(),
                    { timePicker, i, i2 ->
                        reabre.text = LocalTime.of(i, i2, 0).toString()
                    },
                    0,
                    0,
                    true
                ).show()
            }
            fecha?.setOnClickListener {
                TimePickerDialog(
                    requireContext(),
                    { timePicker, i, i2 ->
                        fecha.text = LocalTime.of(i, i2, 0).toString()
                    },
                    0,
                    0,
                    true
                ).show()
            }
            save?.setOnClickListener { }
            bs.show()
        }

        return binding.root
    }
}
