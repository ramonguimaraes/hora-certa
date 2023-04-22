package com.ramonguimaraes.horacerta.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ramonguimaraes.horacerta.databinding.FragmentCreateAccountBinding
import com.ramonguimaraes.horacerta.presenter.authentication.CreateAccountViewModel
import com.ramonguimaraes.horacerta.utils.AccountType
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateAccountFragment : Fragment() {

    private val viewModel: CreateAccountViewModel by viewModel()
    private val binding: FragmentCreateAccountBinding by lazy {
        FragmentCreateAccountBinding.inflate(layoutInflater)
    }
    private val args: CreateAccountFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (args.accountType == AccountType.CLIENT) {
            Toast.makeText(context, "Cliente", Toast.LENGTH_SHORT).show()
        } else if (args.accountType == AccountType.COMPANY) {
            Toast.makeText(context, "Empresa", Toast.LENGTH_SHORT).show()
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }
}
