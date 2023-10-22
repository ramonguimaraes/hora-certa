package com.ramonguimaraes.horacerta.presenter.scheduleClient

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.FragmentClientScheduleBinding
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.presenter.scheduleClient.viewModel.ClientScheduleViewModel
import com.ramonguimaraes.horacerta.presenter.viewUtils.extensions.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class ClientScheduleFragment : Fragment() {

    private val binding: FragmentClientScheduleBinding by lazy {
        FragmentClientScheduleBinding.inflate(layoutInflater)
    }
    private val adapter = ClientScheduleAdapter()
    private val viewModel: ClientScheduleViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel.clientSchedule.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    showLoading()
                }

                is Resource.Success -> {
                    showSuccess(viewModel.sortList(it.result))
                }

                is Resource.Failure -> {
                    binding.viewSwitcher.displayedChild = 0
                    Snackbar.make(
                        binding.root, "Falha ao buscar seus horarios", Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        adapter.setPhoneClick {
            phoneAction(it.phone)
        }
        adapter.setClickListener {
            scheduleActions(it)
        }
        binding.rvClientSchedule.adapter = adapter
        binding.rvClientSchedule.layoutManager = LinearLayoutManager(context)
        binding.swipeLayout.setOnRefreshListener {
            viewModel.load()
            binding.swipeLayout.isRefreshing = false
        }

        return binding.root
    }

    private fun showLoading() {
        binding.viewSwitcher.displayedChild = 1
    }

    private fun showSuccess(appointments: List<ClientAppointment>) {
        if (appointments.isEmpty()) {
            binding.txtEmptyList.visible()
        }
        adapter.submitList(appointments)
        binding.viewSwitcher.displayedChild = 0
    }

    private fun phoneAction(phone: String) {
        PhoneActionBottomSheet(phone).show(parentFragmentManager, "")
    }

    private fun scheduleActions(clientAppointment: ClientAppointment) {
        ScheduleActionBottomSheet(cancel = {
            cancelAlert(clientAppointment)
        }, {
            val action = ClientScheduleFragmentDirections
                .actionClientScheduleToScheduleRegistrationFragment(
                    clientAppointment = clientAppointment,
                    companyUID = clientAppointment.companyUid
                )
            findNavController().navigate(action)
        }).show(
            parentFragmentManager, ""
        )
    }

    private fun cancelAlert(clientAppointment: ClientAppointment) {
        AlertDialog.Builder(context).setTitle("Cancelamento")
            .setMessage("Deseja realmente cancelar seu horario das ${clientAppointment.getHourString()} em ${clientAppointment.companyName}")
            .setPositiveButton("Sim") { dialog, _ ->
                viewModel.cancel(clientAppointment)
                dialog.dismiss()
            }.setNegativeButton("Não") { dialog, _ -> dialog.dismiss() }.show()
    }
}
