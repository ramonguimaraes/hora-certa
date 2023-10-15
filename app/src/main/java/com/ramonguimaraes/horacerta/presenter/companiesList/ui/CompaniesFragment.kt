package com.ramonguimaraes.horacerta.presenter.companiesList.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.FragmentCompaniesBinding
import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.presenter.companiesList.ui.adapter.CompaniesAdapter
import com.ramonguimaraes.horacerta.presenter.companiesList.viewModel.CompaniesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CompaniesFragment : Fragment() {

    private val binding: FragmentCompaniesBinding by lazy {
        FragmentCompaniesBinding.inflate(layoutInflater)
    }
    private val companiesViewModel: CompaniesViewModel by viewModel()
    private val companiesAdapter = CompaniesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        companiesViewModel.companies.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                }

                is Resource.Success -> {
                    companiesAdapter.submitList(it.result)
                }

                is Resource.Failure -> {
                    Toast.makeText(context, "Falha", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.rvCompanies.adapter = companiesAdapter
        binding.rvCompanies.layoutManager = LinearLayoutManager(context)
        return binding.root
    }
}
