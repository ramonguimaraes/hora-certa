package com.ramonguimaraes.horacerta.ui.scheduleConfig

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.BottomSheetRegisterSheduleConfigLayoutBinding
import com.ramonguimaraes.horacerta.presenter.scheduleConfig.viewModel.ScheduleConfigViewModel
import com.ramonguimaraes.horacerta.utils.DayOfWeek
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalTime

class ScheduleConfigBottomSheetDialog : BottomSheetDialogFragment() {

    private val viewModel: ScheduleConfigViewModel by viewModel()
    private val binding: BottomSheetRegisterSheduleConfigLayoutBinding by lazy {
        BottomSheetRegisterSheduleConfigLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding.btnSave.setOnClickListener {
            viewModel.save()
        }

        binding.txtOpenHour.setOnClickListener {
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val localTime = LocalTime.of(hour, minute)
                binding.txtOpenHour.text = localTime.toString()
                viewModel.setOpenHour(localTime)
            }, 0, 0, true).show()
        }

        binding.txtCloseHour.setOnClickListener {
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val localTime = LocalTime.of(hour, minute)
                binding.txtCloseHour.text = localTime.toString()
                viewModel.setCloseHour(localTime)
            }, 0, 0, true).show()
        }

        binding.txtIntervalStart.setOnClickListener {
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val localTime = LocalTime.of(hour, minute)
                binding.txtIntervalStart.text = localTime.toString()
                viewModel.setIntervalStartHour(localTime)
            }, 0, 0, true).show()
        }

        binding.txtIntervalEnd.setOnClickListener {
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val localTime = LocalTime.of(hour, minute)
                binding.txtIntervalEnd.text = localTime.toString()
                viewModel.setIntervalEndHour(localTime)
            }, 0, 0, true).show()
        }

        binding.radioGroup.setOnCheckedChangeListener { _, rb ->
            when (rb) {
                R.id.radioButtonSunday -> viewModel.setDayOfWeek(DayOfWeek.SUNDAY)
                R.id.radioButtonMonday -> viewModel.setDayOfWeek(DayOfWeek.MONDAY)
                R.id.radioButtonTuesday -> viewModel.setDayOfWeek(DayOfWeek.TUESDAY)
                R.id.radioButtonWednesday -> viewModel.setDayOfWeek(DayOfWeek.WEDNESDAY)
                R.id.radioButtonThursday -> viewModel.setDayOfWeek(DayOfWeek.THURSDAY)
                R.id.radioButtonFriday -> viewModel.setDayOfWeek(DayOfWeek.FRIDAY)
                R.id.radioButtonSaturday -> viewModel.setDayOfWeek(DayOfWeek.SATURDAY)
            }
        }

        viewModel.validate.observe(viewLifecycleOwner) { isValid ->
            if (!isValid) {
                Toast.makeText(context, "Os campos estão invalidos", Toast.LENGTH_SHORT).show()
            } else {
                dismiss()
            }
        }

        return binding.root
    }
}
