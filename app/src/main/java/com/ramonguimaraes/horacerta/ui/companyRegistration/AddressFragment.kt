package com.ramonguimaraes.horacerta.ui.companyRegistration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.FragmentAddressBinding
import com.ramonguimaraes.horacerta.domain.base.Result
import com.ramonguimaraes.horacerta.domain.companyRegistration.model.Address
import com.ramonguimaraes.horacerta.presentation.companyRegistration.viewmodel.CompanyRegistrationViewModel
import com.ramonguimaraes.horacerta.ui.validate
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddressFragment : Fragment() {

    private lateinit var mBinding: FragmentAddressBinding
    private val mViewModel: CompanyRegistrationViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAddressBinding.inflate(inflater)
        configSpinnerState()
        setValuesOnScreen()
        mBinding.buttonRegisterAddress.setOnClickListener {
            if (validateFields()) {
                mViewModel.companyView?.address = getAddress()
                mViewModel.saveCompanyData()
                // Cadastra os dados da empresa no firebase e
                // vai para a tela principal da empresa
            }
        }
        observer()
        return mBinding.root
    }

    private fun observer() {
        mViewModel.result.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> showLoading()
                is Result.Success -> handleSuccess()
                is Result.Failure -> handleFailure(it.error)
            }
        }
    }

    private fun showLoading() {
        Toast.makeText(context, "Salvando...", Toast.LENGTH_SHORT).show()
        // TODO IMPLEMENTAR PROGRESSBAR
    }

    private fun hideLoading() {
        // TODO IMPLEMENTAR PROGRESSBAR
    }

    private fun handleSuccess() {
        // vai para a tela de configuração inicial
    }

    private fun handleFailure(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    private fun configSpinnerState() {
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.states,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                mBinding.spinnerState.adapter = adapter
            }
        }
    }

    private fun getAddress(): Address {
        val street = mBinding.editTextTextStreet.text.toString().trim()
        val number = mBinding.editTextTextNumber.text.toString().trim()
        val complement = mBinding.editTextTextComplement.text.toString().trim()
        val district = mBinding.editTextTextDistrict.text.toString().trim()
        val city = mBinding.editTextTextCity.text.toString().trim()
        val state = mBinding.spinnerState.selectedItem as String

        return Address(
            "Brasil",
            state,
            city,
            district,
            street,
            number,
            "",
            "",
            "",
            complement
        )
    }

    private fun setValuesOnScreen() {
        mViewModel.companyView?.address.let {
            mBinding.editTextTextStreet.setText(it?.street)
            mBinding.editTextTextNumber.setText(it?.number)
            mBinding.editTextTextComplement.setText(it?.complement)
            mBinding.editTextTextDistrict.setText(it?.district)
            mBinding.editTextTextCity.setText(it?.city)
        }

        // TODO: Colocar o valor do spinner na tela
    }

    private fun validateFields(): Boolean {
        var isValid = true

        isValid = mBinding.editTextTextCity.validate(isValid = isValid)
        isValid = mBinding.editTextTextComplement.validate(isValid = isValid)
        isValid = mBinding.editTextTextDistrict.validate(isValid = isValid)
        isValid = mBinding.editTextTextNumber.validate(isValid = isValid)
        isValid = mBinding.editTextTextStreet.validate(isValid = isValid)

        return isValid
    }
}
