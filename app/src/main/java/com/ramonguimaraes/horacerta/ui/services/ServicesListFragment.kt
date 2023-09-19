package com.ramonguimaraes.horacerta.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ramonguimaraes.horacerta.databinding.FragmentServicesListBinding
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.presenter.service.ServiceListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ServicesListFragment : Fragment() {

    private val viewModel: ServiceListViewModel by viewModel()
    private val servicesAdapter: ServicesAdapter = ServicesAdapter()
    private val binding: FragmentServicesListBinding by lazy {
        FragmentServicesListBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        configureRecycler()
        observers()
        return binding.root
    }

    private fun observers() {
        viewModel.serviceList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> servicesAdapter.submitList(it.getResultData())
                is Resource.Loading -> Toast.makeText(context, "carregando", Toast.LENGTH_SHORT)
                    .show()

                else -> Toast.makeText(
                    context,
                    "Houve um problema ao recuperar a lista",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun configureRecycler() {
        binding.rvServices.apply {
            adapter = servicesAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}
