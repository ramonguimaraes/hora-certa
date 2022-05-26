package com.ramonguimaraes.horacerta.ui.clientProfileFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
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

        val email = GoogleSignIn.getLastSignedInAccount(requireContext())?.email
        val displayName = GoogleSignIn.getLastSignedInAccount(requireContext())?.displayName
        mBinding.editTextClientProfileEmail.setText(email)
        mBinding.editTextClientProfileName.setText(displayName)

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
                is Result.Loading -> showProgress()
                is Result.Success -> doSuccess()
                is Result.Failure -> doFailure(it.error)
            }
        }
    }

    private fun doSuccess() {
        hideProgress()
        Toast.makeText(context, "Perfil criado com sucesso", Toast.LENGTH_SHORT).show()
        // VAI PARA A PROXIMA TELA E FAZ O QUE TEM QUE FAZER
    }

    private fun doFailure(error: String) {
        hideProgress()
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    private fun hideProgress() {
        mBinding.progressBar.visibility = View.GONE
    }

    private fun showProgress() {
        mBinding.progressBar.visibility = View.VISIBLE
    }
}
