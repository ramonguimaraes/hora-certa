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
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.scheduleConfig.model.ScheduleConfig
import com.ramonguimaraes.horacerta.presenter.scheduleConfig.viewModel.ScheduleConfigViewModel
import com.ramonguimaraes.horacerta.utils.DayOfWeek
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalTime

class ScheduleConfigBottomSheetDialog(private val function: () -> Unit, private val resultData: List<ScheduleConfig>?) : BottomSheetDialogFragment() {

    private val viewModel: ScheduleConfigViewModel by viewModel()
    private val binding: BottomSheetRegisterSheduleConfigLayoutBinding by lazy {
        BottomSheetRegisterSheduleConfigLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.daysOfWeek = resultData?.map { it.dayOfWeek } ?: emptyList()
        binding.btnSave.setOnClickListener {
            viewModel.save()
        }

        binding.txtOpenHour.setOnClickListener {
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val localTime = LocalTime.of(hour, minute)
                viewModel.setOpenHour(localTime)
            }, 0, 0, true).show()
        }

        binding.txtCloseHour.setOnClickListener {
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val localTime = LocalTime.of(hour, minute)
                viewModel.setCloseHour(localTime)
            }, 0, 0, true).show()
        }

        binding.txtIntervalStart.setOnClickListener {
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val localTime = LocalTime.of(hour, minute)
                viewModel.setIntervalStartHour(localTime)
            }, 0, 0, true).show()
        }

        binding.txtIntervalEnd.setOnClickListener {
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val localTime = LocalTime.of(hour, minute)
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
            }
        }

        viewModel.saveResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> showSuccess()
                else -> showFailure()
            }
        }
        return binding.root
    }

    private fun showFailure() {
        Toast.makeText(context, "Falha ao salvar", Toast.LENGTH_SHORT).show()
        dismiss()
    }

    private fun showSuccess() {
        function.invoke()
        Toast.makeText(context, "Salvo com sucesso", Toast.LENGTH_SHORT).show()
        dismiss()
    }
}
