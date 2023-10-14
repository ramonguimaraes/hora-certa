package com.ramonguimaraes.horacerta.presenter.scheduleConfig.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ramonguimaraes.horacerta.databinding.FragmentListScheduleConfigBinding
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.scheduleConfig.model.ScheduleConfig
import com.ramonguimaraes.horacerta.presenter.scheduleConfig.viewModel.ScheduleConfigListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScheduleConfigListFragment : Fragment() {

    private val binding: FragmentListScheduleConfigBinding by lazy {
        FragmentListScheduleConfigBinding.inflate(layoutInflater)
    }
    private val scheduleConfigAdapter = ScheduleConfigAdapter()
    private val viewModel: ScheduleConfigListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.scheduleConfigList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> updateList(it.result)
                is Resource.Failure -> showFailure()
            }
        }

        binding.fabAddConfig.setOnClickListener {
            if (viewModel.verifyListSize()) {
                showToast("Você já configurou todos os dias")
                return@setOnClickListener
            }
            showBottomSheet(
                { viewModel.loadScheduleConfigList() },
                viewModel.scheduleConfigList.value?.getResultData(), null
            )
        }

        scheduleConfigAdapter.setOnClick { scheduleConfig ->
            showBottomSheet(
                { viewModel.loadScheduleConfigList() },
                viewModel.scheduleConfigList.value?.getResultData(), scheduleConfig
            )
        }

        configureRecyclerView()
        return binding.root
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun updateList(result: List<ScheduleConfig>) {
        scheduleConfigAdapter.submitList(result.toMutableList())
    }

    private fun showFailure() {
        showToast("Falha")
    }

    private fun showLoading() {
        showToast("Carregando")
    }

    private fun configureRecyclerView() {
        binding.rvScheduleConfig.apply {
            adapter = scheduleConfigAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun showBottomSheet(
        function: () -> Unit,
        resultData: List<ScheduleConfig>?,
        scheduleConfig: ScheduleConfig?
    ) {
        ScheduleConfigBottomSheetDialog(function, resultData, scheduleConfig)
            .show(parentFragmentManager, "scheduleConfigBottomSheet")
    }
}
