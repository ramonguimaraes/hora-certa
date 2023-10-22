package com.ramonguimaraes.horacerta.presenter.companiesList.ui

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ramonguimaraes.horacerta.databinding.FragmentCompaniesBinding
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.presenter.companiesList.ui.adapter.CompaniesAdapter
import com.ramonguimaraes.horacerta.presenter.companiesList.viewModel.CompaniesViewModel
import com.ramonguimaraes.horacerta.presenter.viewUtils.extensions.gone
import com.ramonguimaraes.horacerta.presenter.viewUtils.extensions.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class CompaniesFragment : Fragment() {

    private val binding: FragmentCompaniesBinding by lazy {
        FragmentCompaniesBinding.inflate(layoutInflater)
    }
    private val companiesViewModel: CompaniesViewModel by viewModel()
    private val companiesAdapter = CompaniesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureSegmentsSpinner()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        companiesViewModel.companies.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    showLoading()
                    companiesAdapter.setCompanyList(emptyList())
                }

                is Resource.Success -> {
                    hideLoading()
                    companiesAdapter.setCompanyList(it.result)
                }

                is Resource.Failure -> {
                    hideLoading()
                    showError()
                }
            }
        }

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                companiesAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                companiesAdapter.filter.filter(newText)
                return true
            }
        })

        companiesAdapter.setClickListener {
            openScheduleRegistration(it.companyUid)
        }
        binding.rvCompanies.adapter = companiesAdapter
        binding.rvCompanies.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    private fun openScheduleRegistration(companyUID: String) {
        val action = CompaniesFragmentDirections.actionCompaniesToScheduleRegistrationFragment(companyUID = companyUID)
        findNavController().navigate(action)

    }

    private fun showLoading() {
        binding.progressBar.visible()
        binding.spnSegments.isEnabled = false
    }

    private fun hideLoading() {
        binding.progressBar.gone()
        binding.spnSegments.isEnabled = true
    }

    private fun showError() {
        Toast.makeText(context, "Falha", Toast.LENGTH_SHORT).show()
    }

    private fun configureSegmentsSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_dropdown_item_1line,
            companiesViewModel.segments
        )

        binding.spnSegments.adapter = adapter
        binding.spnSegments.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val segment = binding.spnSegments.selectedItem.toString()
                companiesViewModel.loadBySegment(segment)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
}
