package com.ramonguimaraes.horacerta.ui.authentication.mainLogin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.FragmentCompanyLoginBinding

class CompanyLoginFragment : Fragment() {

    private lateinit var mBinding: FragmentCompanyLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentCompanyLoginBinding.inflate(inflater)

        mBinding.buttonLogin.setOnClickListener {
            login(
                mBinding.editTextTextLoginEmail.text.toString(),
                mBinding.editTextTextLoginPassword.text.toString()
            )
        }

        mBinding.textViewCompanyRegistration.setOnClickListener {
            it.findNavController().navigate(R.id.action_companyLoginFragment_to_registerFragment)
        }

        return mBinding.root
    }

    private fun login(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Logado..", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("firebaseErro", "${it.exception.toString()} :: ${it.exception?.cause}")
                }
            }
    }
}
