package com.ramonguimaraes.horacerta.ui.schedule

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.FragmentScheduleRegistrationBinding
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.schedule.model.TimeInterval
import com.ramonguimaraes.horacerta.presenter.schedule.ScheduleRegistrationViewModel
import com.ramonguimaraes.horacerta.ui.schedule.adapter.AvailableHorsAdapter
import com.ramonguimaraes.horacerta.ui.schedule.adapter.ServicesAdapter
import com.ramonguimaraes.horacerta.utils.formattedDate
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class ScheduleRegistrationFragment : Fragment() {

    private val viewModel: ScheduleRegistrationViewModel by viewModel()
    private val binding: FragmentScheduleRegistrationBinding by lazy {
        FragmentScheduleRegistrationBinding.inflate(layoutInflater)
    }
    private var availableHorsAdapter: AvailableHorsAdapter = AvailableHorsAdapter()
    private var servicesAdapter: ServicesAdapter = ServicesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val companyUID = arguments?.getString("companyUID")

        companyUID?.let {
            viewModel.setCompanyUid(it)
        }

        viewModel.setDate()
        viewModel.load()
        viewModel.loadServices()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.txtSelectedDate.text = viewModel.getToday().formattedDate()
        configureRecycler()
        viewModel.liveData.observe(viewLifecycleOwner) {
            availableHorsAdapter.submitList(it.toMutableList())
        }

        viewModel.services.observe(viewLifecycleOwner) {
            servicesAdapter.submitList(it)
        }

        servicesAdapter.setCheckListener { item, isChecked ->
            viewModel.updateTotalTime(item, isChecked)
        }

        binding.rvServices.adapter = servicesAdapter
        binding.rvServices.layoutManager = LinearLayoutManager(context)

        viewModel.date.observe(viewLifecycleOwner) {
            binding.txtSelectedDate.text = it.formattedDate()
            viewModel.load(it)
        }

        binding.btnOpenCalendar.setOnClickListener {
            datePickerDialog()
        }

        viewModel.totalTime.observe(viewLifecycleOwner) {
            viewModel.configuraListaDeHoras(it)
        }

        availableHorsAdapter.setOnClick {
            showConfirmeDialog(it)
        }

        viewModel.saveResult.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> activity?.supportFragmentManager?.popBackStackImmediate()
                is Resource.Loading -> {
                    // TODO Implementar loading
                }

                is Resource.Failure -> {
                    // TODO Implementar mensagem de erro
                }
            }
        }

        return binding.root
    }

    private fun showConfirmeDialog(timeInterval: TimeInterval) {
        val message = resources.getString(R.string.alert_message, timeInterval.time)
        AlertDialog.Builder(context)
            .setTitle("Agendamento")
            .setMessage(message)
            .setPositiveButton("Agendar") { dialog, _ ->
                viewModel.save(timeInterval)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
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
