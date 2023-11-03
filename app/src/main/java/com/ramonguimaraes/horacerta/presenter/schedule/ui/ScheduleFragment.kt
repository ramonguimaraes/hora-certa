package com.ramonguimaraes.horacerta.presenter.schedule.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.FragmentScheduleBinding
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.schedule.model.Appointment
import com.ramonguimaraes.horacerta.domain.services.model.Service
import com.ramonguimaraes.horacerta.presenter.cashFlow.model.PaymentMethod
import com.ramonguimaraes.horacerta.presenter.schedule.viewModel.ScheduleViewModel
import com.ramonguimaraes.horacerta.presenter.schedule.ui.adapter.ScheduleAdapter
import com.ramonguimaraes.horacerta.presenter.scheduleClient.ClientAppointment
import com.ramonguimaraes.horacerta.presenter.scheduleClient.ClientScheduleFragmentDirections
import com.ramonguimaraes.horacerta.presenter.scheduleClient.ScheduleActionBottomSheet
import com.ramonguimaraes.horacerta.presenter.viewUtils.extensions.gone
import com.ramonguimaraes.horacerta.utils.extensions.onlyDate
import com.ramonguimaraes.horacerta.presenter.viewUtils.extensions.visible
import com.ramonguimaraes.horacerta.utils.extensions.isRetroactive
import com.ramonguimaraes.horacerta.utils.extensions.isToday
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

        mBinding.cash.setOnClickListener {
            showPopupMenu(it)
        }

        scheduleAdapter.setConfirmListener {
            scheduleActions(it)
        }

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

    private fun scheduleActions(appointment: Appointment) {
        ScheduleActionBottomSheet(
            showConfirm = appointment.date.isToday(),
            showCancel = !appointment.date.isRetroactive(),
            cancel = {
                cancelAlert(appointment)
            },
            reschedule = {
                val action = ScheduleFragmentDirections
                    .actionScheduleToScheduleRegistrationFragment(
                        companyUID = scheduleViewModel.companyUid,
                        appointmentId = appointment.id,
                        services = appointment.services.toTypedArray(),
                        scheduledTimes = appointment.scheduledTimes.toTypedArray(),
                        date = appointment.date.timeInMillis
                    )
                findNavController().navigate(action)
            },
            confirm = {
                confirmAlert(appointment)
            }
        ).show(
            parentFragmentManager, ""
        )
    }

    private fun confirmAlert(appointment: Appointment) {
        var paymentMethod: PaymentMethod = PaymentMethod.PIX
        val array = arrayOf("Pix", "Cartão", "Dinheiro")

         AlertDialog.Builder(context)
        .setTitle("Selecione um método de pagamento")
        .setSingleChoiceItems(array, 0) { _, selectedItem ->
            paymentMethod = when (selectedItem) {
                0 -> PaymentMethod.PIX
                1 -> PaymentMethod.CASH
                2 -> PaymentMethod.CARD
                else -> PaymentMethod.CARD
            }
        }
        .setPositiveButton("confirmar") { dialog, _ ->
            scheduleViewModel.confirmClient(appointment, paymentMethod)
            dialog.dismiss()
        }
        .create()
        .show()
    }

    private fun cancelAlert(appointment: Appointment) {
        AlertDialog.Builder(context).setTitle("Cancelamento")
            .setMessage("Deseja realmente cancelar o horario das ${appointment.getHourString()} do cliente ${appointment.clientName}")
            .setPositiveButton("Sim") { dialog, _ ->
                scheduleViewModel.cancel(appointment)
                dialog.dismiss()
            }.setNegativeButton("Não") { dialog, _ -> dialog.dismiss() }.show()
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
        val action = ScheduleFragmentDirections
            .actionScheduleToScheduleRegistrationFragment(companyUID = scheduleViewModel.companyUid)
        findNavController().navigate(action)
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.schedule_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.cash_register -> {
                    findNavController().navigate(R.id.action_schedule_to_cashFlowFragment)
                    true
                }

                else -> false
            }
        }

        popupMenu.show()
    }
}
