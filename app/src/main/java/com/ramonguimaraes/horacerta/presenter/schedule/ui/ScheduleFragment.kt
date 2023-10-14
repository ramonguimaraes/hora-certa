package com.ramonguimaraes.horacerta.presenter.schedule.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.FragmentScheduleBinding
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.presenter.schedule.viewModel.ScheduleViewModel
import com.ramonguimaraes.horacerta.ui.schedule.adapter.ScheduleAdapter
import com.ramonguimaraes.horacerta.utils.gone
import com.ramonguimaraes.horacerta.utils.onlyDate
import com.ramonguimaraes.horacerta.utils.visible
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
                    hideLoading()
                    scheduleAdapter.submitList(it.result)
                }

                is Resource.Loading -> showLoading()
                else -> {
                    hideLoading()
                }
            }
        }

        configureRecycler()
        return mBinding.root
    }

    private fun showLoading() {
        mBinding.progressBar.visible()
    }

    private fun hideLoading() {
        mBinding.progressBar.gone()
    }

    override fun onResume() {
        super.onResume()
        scheduleViewModel.setSelectedDate(Calendar.getInstance().onlyDate())
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
        val args = Bundle()
        args.putString("companyUID", scheduleViewModel.companyUid)

        val fragment = ScheduleRegistrationFragment()
        fragment.arguments = args

        val supportFragmentManager = activity?.supportFragmentManager
        supportFragmentManager?.beginTransaction()?.replace(
            R.id.fragmentContainerView,
            fragment,
            "scheduleRegistrationFragment"
        )?.addToBackStack(null)?.commit()
    }
}
