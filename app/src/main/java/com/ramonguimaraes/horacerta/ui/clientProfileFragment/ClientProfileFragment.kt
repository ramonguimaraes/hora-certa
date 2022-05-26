package com.ramonguimaraes.horacerta.ui.clientProfileFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.ramonguimaraes.horacerta.databinding.FragmentClientProfileBinding
import com.ramonguimaraes.horacerta.domain.base.Result
import com.ramonguimaraes.horacerta.presentation.clientProfile.ClientProfileViewModel
import com.ramonguimaraes.horacerta.presentation.clientProfile.model.ClientView
import org.koin.androidx.viewmodel.ext.android.viewModel

class ClientProfileFragment : Fragment() {

    private val mViewModel: ClientProfileViewModel by viewModel()
    private lateinit var mBinding: FragmentClientProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentClientProfileBinding.inflate(inflater)
        mBinding.buttonProfileNext.setOnClickListener {
            saveClientProfile()
        }
        observer()
        return mBinding.root
    }

    private fun saveClientProfile() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val name = mBinding.editTextClientProfileName.text.toString()
        val phone = mBinding.editTextClientProfilePhone.text.toString()
        val email = mBinding.editTextClientProfileEmail.text.toString()

        val client = ClientView(uid, name, phone, email)

        mViewModel.saveClient(client)
    }

    private fun observer() {
        mViewModel.result.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> Toast.makeText(context, "Carregando", Toast.LENGTH_SHORT)
                    .show()
                is Result.Success -> Toast.makeText(context, it.data, Toast.LENGTH_SHORT).show()
                is Result.Failure -> Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
