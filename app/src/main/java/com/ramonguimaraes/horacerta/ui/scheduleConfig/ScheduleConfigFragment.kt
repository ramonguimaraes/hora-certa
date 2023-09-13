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
            ScheduleConfigBottomSheetDialog().show(parentFragmentManager, "ScheduleConfigBottomSheetDialog")
        }
        return binding.root
    }
}
