package com.ramonguimaraes.horacerta.presenter.companyProfile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ramonguimaraes.horacerta.databinding.FragmentAddressBinding
import com.ramonguimaraes.horacerta.presenter.companyProfile.viewModel.CompanyProfileViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AddressBottomSheet : BottomSheetDialogFragment() {

    private val viewModel: CompanyProfileViewModel by sharedViewModel()
    private val binding: FragmentAddressBinding by lazy {
        FragmentAddressBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.btnSave.setOnClickListener {
            viewModel.saveAddress()
        }
        viewModel.dismissDialog.observe(viewLifecycleOwner) {
            if (it) {
                dismiss()
                viewModel.dismissDialog.postValue(false)
            }
        }
        return binding.root
    }
}
