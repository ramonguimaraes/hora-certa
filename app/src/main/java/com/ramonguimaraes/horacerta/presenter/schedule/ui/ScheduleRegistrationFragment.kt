package com.ramonguimaraes.horacerta.presenter.schedule.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.FragmentScheduleRegistrationBinding
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.schedule.model.TimeInterval
import com.ramonguimaraes.horacerta.presenter.schedule.ui.adapter.AvailableHorsAdapter
import com.ramonguimaraes.horacerta.presenter.schedule.ui.adapter.ServicesAdapter
import com.ramonguimaraes.horacerta.presenter.schedule.viewModel.ScheduleRegistrationViewModel
import com.ramonguimaraes.horacerta.utils.extensions.formattedDate
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class ScheduleRegistrationFragment : Fragment() {

    private val viewModel: ScheduleRegistrationViewModel by viewModel()
    private var availableHorsAdapter: AvailableHorsAdapter = AvailableHorsAdapter()
    private var servicesAdapter: ServicesAdapter = ServicesAdapter()
    private val binding: FragmentScheduleRegistrationBinding by lazy {
        FragmentScheduleRegistrationBinding.inflate(layoutInflater)
    }
    private val args: ScheduleRegistrationFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val companyUID = args.companyUID

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

        viewModel.services.observe(viewLifecycleOwner) { services ->
            /*
            if (args.clientAppointment != null) {
                val checkedIds = args.clientAppointment?.services?.map { it.id } ?: listOf()
                val checkedServices = viewModel.setCheckedServices(services, checkedIds)
                servicesAdapter.submitList(checkedServices)
                return@observe
            }
            */

            if (!args.services.isNullOrEmpty()) {
                val checkedIds = args.services?.map { it.id } ?: listOf()
                val checkedServices = viewModel.setCheckedServices(services, checkedIds)
                servicesAdapter.submitList(checkedServices)
                return@observe
            }

            servicesAdapter.submitList(services)
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
            if (args.appointmentId != null) {
                showRescheduleDialogDialog(it)
                return@setOnClick
            }

            showConfirmDialog(it)
        }

        binding.backButton.setOnClickListener {
            activity?.supportFragmentManager?.popBackStackImmediate()
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

        /*
        if (args.clientAppointment != null) {
            val year = args.clientAppointment?.date?.get(Calendar.YEAR)
            val mouth = args.clientAppointment?.date?.get(Calendar.MONTH)
            val dayOfMonth = args.clientAppointment?.date?.get(Calendar.DAY_OF_MONTH)

            if (year != null && mouth != null && dayOfMonth != null) {
                viewModel.setDate(year, mouth, dayOfMonth)
            }
        }
        */

        if (args.date != -1L) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = args.date

            val year = calendar.get(Calendar.YEAR)
            val mouth = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            viewModel.setDate(year, mouth, dayOfMonth)
        }
        return binding.root
    }

    private fun showConfirmDialog(timeInterval: TimeInterval) {
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

    private fun showRescheduleDialogDialog(timeInterval: TimeInterval) {
        val message = resources.getString(R.string.reschedule_message, timeInterval.time)
        AlertDialog.Builder(context)
            .setTitle("Reagendamento")
            .setMessage(message)
            .setPositiveButton("Reagendar") { dialog, _ ->
                /*
                args.clientAppointment?.let { clientAppointment ->

                    viewModel.reschedule(timeInterval, clientAppointment)
                }
                */

                args.appointmentId?.let {
                    viewModel.reschedule(
                        timeInterval,
                        args.appointmentId ?: "",
                        args.scheduledTimes?.toList() ?: listOf()
                    )
                }
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
