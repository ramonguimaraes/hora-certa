package com.ramonguimaraes.horacerta.ui.schedule

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.FragmentScheduleBinding
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.presenter.schedule.ScheduleViewModel
import com.ramonguimaraes.horacerta.ui.schedule.adapter.ScheduleAdapter
import com.ramonguimaraes.horacerta.ui.schedule.adapter.onlyDate
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class ScheduleFragment : Fragment() {

    private val mBinding: FragmentScheduleBinding by lazy {
        FragmentScheduleBinding.inflate(layoutInflater)
    }
    private val scheduleAdapter = ScheduleAdapter()
    private val scheduleViewModel: ScheduleViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding.viewModel = scheduleViewModel
        mBinding.lifecycleOwner = viewLifecycleOwner

        scheduleViewModel.showDatePickerEvent.observe(viewLifecycleOwner) {
            if (it) {
                configureDatePickerDialog(Calendar.getInstance()) { calendar ->
                    scheduleViewModel.setSelectedDate(calendar)
                    scheduleViewModel.load(calendar.onlyDate())
                }.show()
            }
        }

        scheduleViewModel.openScheduleRegistration.observe(viewLifecycleOwner) {
            if (it) {
                openScheduleRegistration()
            }
        }

        scheduleViewModel.appointment.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    scheduleAdapter.submitList(it.result)
                }

                else -> {}
            }
        }

        configureRecycler()
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        scheduleViewModel.load(Calendar.getInstance().onlyDate())
    }

    private fun configureRecycler() {
        mBinding.rvSchedule.adapter = scheduleAdapter
    }

    private fun configureDatePickerDialog(
        calendar: Calendar,
        onDateSet: (Calendar) -> Unit
    ): DatePickerDialog {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                onDateSet(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        return datePickerDialog
    }

    private fun openScheduleRegistration() {
        val supportFragmentManager = activity?.supportFragmentManager
        supportFragmentManager?.beginTransaction()?.replace(
            R.id.fragmentContainerView,
            ScheduleRegistrationFragment(),
            "scheduleRegistrationFragment"
        )?.addToBackStack(null)?.commit()
    }
}
