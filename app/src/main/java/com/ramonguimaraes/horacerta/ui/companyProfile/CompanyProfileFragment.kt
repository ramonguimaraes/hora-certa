package com.ramonguimaraes.horacerta.ui.companyProfile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.FragmentCompanyProfileBinding
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.presenter.companyProfile.CompanyProfileViewModel
import com.ramonguimaraes.horacerta.ui.home.HomeFragment
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

        val registry = takePictureForResult()
        binding.imgProfilePic.setOnClickListener {
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
        return binding.root
    }

    private fun saveObserver() {
        viewModel.saveResult.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    Toast.makeText(context, "Salvando...", Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    if (it.result) {
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
        val supportFragmentManager = activity?.supportFragmentManager
        supportFragmentManager?.beginTransaction()?.replace(
            R.id.fragmentContainerView,
            HomeFragment(),
            "homeFragment"
        )?.addToBackStack(null)?.commit()
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
