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

class ScheduleConfigBottomSheetDialog(
    private val function: () -> Unit,
    private val resultData: List<ScheduleConfig>?,
    private val scheduleConfig: ScheduleConfig? = null
) : BottomSheetDialogFragment() {

    private val viewModel: ScheduleConfigViewModel by viewModel()
    private val binding: BottomSheetRegisterSheduleConfigLayoutBinding by lazy {
        BottomSheetRegisterSheduleConfigLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel.daysOfWeek = resultData?.map { it.dayOfWeek } ?: emptyList()
        scheduleConfig?.let {
            viewModel.scheduleConfig.value = it
            viewModel.isUpdating = true
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.btnSave.setOnClickListener {
            viewModel.save()
        }

        binding.txtOpenHour.setOnClickListener {
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val localTime = LocalTime.of(hour, minute)
                viewModel.setOpenTime(localTime)
            }, 0, 0, true).show()
        }

        binding.txtCloseHour.setOnClickListener {
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val localTime = LocalTime.of(hour, minute)
                viewModel.setCloseTime(localTime)
            }, 0, 0, true).show()
        }

        binding.txtIntervalStart.setOnClickListener {
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val localTime = LocalTime.of(hour, minute)
                viewModel.setIntervalStart(localTime)
            }, 0, 0, true).show()
        }

        binding.txtIntervalEnd.setOnClickListener {
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val localTime = LocalTime.of(hour, minute)
                viewModel.setIntervalEnd(localTime)
            }, 0, 0, true).show()
        }

        checkDayOfWeek()
        checkListenerDayOfWeek()
        observers()

        return binding.root
    }

    private fun checkListenerDayOfWeek() {

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
    }

    private fun checkDayOfWeek() {
        binding.radioGroup.check(
            when (viewModel.scheduleConfig.value?.dayOfWeek) {
                DayOfWeek.SUNDAY -> R.id.radioButtonSunday
                DayOfWeek.MONDAY -> R.id.radioButtonMonday
                DayOfWeek.TUESDAY -> R.id.radioButtonTuesday
                DayOfWeek.WEDNESDAY -> R.id.radioButtonWednesday
                DayOfWeek.THURSDAY -> R.id.radioButtonThursday
                DayOfWeek.FRIDAY -> R.id.radioButtonFriday
                DayOfWeek.SATURDAY -> R.id.radioButtonSaturday
                else -> R.id.radioButtonMonday
            }
        )
    }

    private fun observers() {
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
