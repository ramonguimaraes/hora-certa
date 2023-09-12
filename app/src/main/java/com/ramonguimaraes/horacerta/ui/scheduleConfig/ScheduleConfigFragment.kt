package com.ramonguimaraes.horacerta.ui.scheduleConfig

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.FragmentScheduleConfigBinding
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.scheduleConfig.model.ScheduleConfig
import com.ramonguimaraes.horacerta.presenter.scheduleConfig.viewModel.ScheduleConfigViewModel
import com.ramonguimaraes.horacerta.utils.DayOfWeek
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalTime

class ScheduleConfigFragment : Fragment() {
    val binding: FragmentScheduleConfigBinding by lazy {
        FragmentScheduleConfigBinding.inflate(layoutInflater)
    }

    private val viewModel: ScheduleConfigViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.saveResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {}
                is Resource.Loading -> {}
                is Resource.Failure -> {}
                else -> {}
            }
        }

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
                TimePickerDialog(requireContext(), { _, hour, minute ->
                    val localTime = LocalTime.of(hour, minute)
                    open.text = localTime.toString()
                    viewModel.setOpenHour(localTime)
                }, 0, 0, true).show()
            }

            close?.setOnClickListener {
                TimePickerDialog(requireContext(), { _, hour, minute ->
                    val localTime = LocalTime.of(hour, minute)
                    close.text = localTime.toString()
                    viewModel.setIntervalStartHour(localTime)
                }, 0, 0, true).show()
            }

            reabre?.setOnClickListener {
                TimePickerDialog(requireContext(), { _, hour, minute ->
                    val localTime = LocalTime.of(hour, minute)
                    reabre.text = localTime.toString()
                    viewModel.setIntervalEndHour(localTime)
                }, 0, 0, true).show()
            }

            fecha?.setOnClickListener {
                TimePickerDialog(requireContext(), { _, hour, minute ->
                    val localTime = LocalTime.of(hour, minute)
                    fecha.text = localTime.toString()
                    viewModel.setCloseHour(localTime)
                }, 0, 0, true).show()
            }

            save?.setOnClickListener {
                viewModel.save()
                bs.dismiss()
            }

            bs.findViewById<RadioGroup>(R.id.radioGroup)
                ?.setOnCheckedChangeListener { _, button ->
                    val dayOfWeek = when (button) {
                        R.id.radioButtonSunday -> DayOfWeek.SUNDAY
                        R.id.radioButtonMonday -> DayOfWeek.MONDAY
                        R.id.radioButtonTuesday -> DayOfWeek.TUESDAY
                        R.id.radioButtonWednesday -> DayOfWeek.WEDNESDAY
                        R.id.radioButtonThursday -> DayOfWeek.THURSDAY
                        R.id.radioButtonFriday -> DayOfWeek.FRIDAY
                        R.id.radioButtonSaturday -> DayOfWeek.SATURDAY
                        else -> null
                    }

                    viewModel.setDayOfWeek(dayOfWeek)
                }

            bs.show()
        }
        return binding.root
    }
}
