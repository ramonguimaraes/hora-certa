package com.ramonguimaraes.horacerta.ui.schedule

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.ramonguimaraes.horacerta.databinding.FragmentScheduleRegistrationBinding
import com.ramonguimaraes.horacerta.presenter.schedule.ScheduleRegistrationViewModel
import com.ramonguimaraes.horacerta.ui.schedule.adapter.AvailableHorsAdapter
import com.ramonguimaraes.horacerta.utils.formattedDate
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class ScheduleRegistrationFragment : Fragment() {

    private val viewModel: ScheduleRegistrationViewModel by viewModel()
    private val binding: FragmentScheduleRegistrationBinding by lazy {
        FragmentScheduleRegistrationBinding.inflate(layoutInflater)
    }
    private var availableHorsAdapter: AvailableHorsAdapter = AvailableHorsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setDate()
        viewModel.load()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.txtSelectedDate.text = viewModel.getToday().formattedDate()
        configureRecycler()
        viewModel.liveData.observe(viewLifecycleOwner) {
            availableHorsAdapter.submitList(it)
        }

        viewModel.date.observe(viewLifecycleOwner) {
            binding.txtSelectedDate.text = it.formattedDate()
            viewModel.load(it)
        }

        binding.btnOpenCalendar.setOnClickListener {
            datePickerDialog()
        }

        availableHorsAdapter.setOnClick {
            viewModel.save(it)
        }

        return binding.root
    }

    private fun configureRecycler() {
        binding.rvAvailableHours.adapter = availableHorsAdapter
        binding.rvAvailableHours.layoutManager = GridLayoutManager(context, 3)
    }

    private fun datePickerDialog() {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                viewModel.setDate(year, month, dayOfMonth)
            },
            year, month, dayOfMonth
        ).show()
    }
}
