package com.ramonguimaraes.horacerta.presenter.companyProfile.ui

import android.net.Uri
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.FragmentCompanyProfileBinding
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.user.model.AccountType
import com.ramonguimaraes.horacerta.presenter.companyProfile.viewModel.CompanyProfileViewModel
import com.ramonguimaraes.horacerta.presenter.viewUtils.Mask
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CompanyProfileFragment : Fragment() {

    private val viewModel: CompanyProfileViewModel by sharedViewModel()
    private val binding: FragmentCompanyProfileBinding by lazy {
        FragmentCompanyProfileBinding.inflate(layoutInflater)
    }
    private val args: CompanyProfileFragmentArgs by navArgs()
    private var phoneMask: TextWatcher? = null
    private var cnpjMask: TextWatcher? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        configureBinding()
        configureSegmentsSpinner()

        binding.editAddress.setOnClickListener {
            val frag = MapsFragment()
            val supportFragmentManager = activity?.supportFragmentManager
            supportFragmentManager?.beginTransaction()?.replace(
                R.id.fragmentContainerView,
                frag,
                "MAPS_FRAGMENT"
            )?.addToBackStack(null)?.commit()
        }

        viewModel.profileViewState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    if (args.callFromLogin && it.result != null && it.result.id.isNotBlank()) {
                        goToHome()
                        return@observe
                    }
                    binding.viewSwitcher.displayedChild = 1
                }

                is Resource.Failure -> {
                    showError()
                }

                is Resource.Loading -> {
                    showLoading()
                }
            }
        }

        val registry = takePictureForResult()
        binding.openGallery.setOnClickListener {
            registry.launch(
                CropImageContractOptions(
                    Uri.EMPTY,
                    CropImageOptions(
                        imageSourceIncludeCamera = false,
                        cropShape = CropImageView.CropShape.OVAL,
                        fixAspectRatio = true
                    )
                )
            )
        }
        saveObserver()

        if (phoneMask != null) {
            binding.edtPhone.removeTextChangedListener(phoneMask)
            binding.edtPhone.addTextChangedListener(phoneMask)
            binding.edtPhone.setText("")
        } else {
            phoneMask = Mask.mask(Mask.PHONE_PATERN, binding.edtPhone)
            binding.edtPhone.addTextChangedListener(phoneMask)
            binding.edtPhone.setText("")
        }

        if (cnpjMask != null) {
            binding.edtCnpj.removeTextChangedListener(cnpjMask)
            binding.edtCnpj.addTextChangedListener(cnpjMask)
            binding.edtCnpj.setText("")
        } else {
            cnpjMask = Mask.mask(Mask.CNPJ_PATERN, binding.edtCnpj)
            binding.edtCnpj.addTextChangedListener(cnpjMask)
            binding.edtCnpj.setText("")
        }

        return binding.root
    }

    private fun showLoading() {
        binding.viewSwitcher.displayedChild = 0
    }

    private fun showError() {
        binding.viewSwitcher.displayedChild = 1
    }

    private fun saveObserver() {
        viewModel.saveResult.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    Toast.makeText(context, "Salvando...", Toast.LENGTH_SHORT).show()
                }

                is Resource.Success -> {
                    if (args.callFromLogin && it.result) {
                        goToHome()
                    }
                }

                is Resource.Failure -> {
                    Toast.makeText(context, "Falha ao salvar perfil", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goToHome() {
        val action =
            CompanyProfileFragmentDirections.actionCompanyProfileFragmentToHomeFragment(accountType = AccountType.COMPANY)
        findNavController().navigate(action)
    }

    private fun takePictureForResult(): ActivityResultLauncher<CropImageContractOptions> {
        return registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                viewModel.setProfilePicUri(result.uriContent)
            }
        }
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

        viewModel.profileView.observe(viewLifecycleOwner) {
            for (i in 0 until adapter.count) {
                if (adapter.getItem(i) == viewModel.profileView.value?.companySegment) {
                    binding.spnSegments.setSelection(i)
                    break
                }
            }
        }
    }
}
