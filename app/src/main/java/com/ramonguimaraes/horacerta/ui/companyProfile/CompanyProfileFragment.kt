package com.ramonguimaraes.horacerta.ui.companyProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.ramonguimaraes.horacerta.databinding.FragmentCompanyProfileBinding
import com.ramonguimaraes.horacerta.presenter.companyProfile.CompanyProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CompanyProfileFragment : Fragment() {

    private val viewModel: CompanyProfileViewModel by viewModel()
    private val binding: FragmentCompanyProfileBinding by lazy {
        FragmentCompanyProfileBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        configureBinding()
        configureSegmentsSpinner()
        return binding.root
    }

    private fun configureBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun configureSegmentsSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            viewModel.segments
        )

        binding.spnSegments.adapter = adapter
        binding.spnSegments.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.setSegment(
                    binding.spnSegments.selectedItem.toString()
                )
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        viewModel.profile.observe(viewLifecycleOwner) {
            for (i in 0 until adapter.count) {
                if (adapter.getItem(i) == viewModel.profile.value?.companySegment) {
                    binding.spnSegments.setSelection(i)
                    break
                }
            }
        }
    }
}
