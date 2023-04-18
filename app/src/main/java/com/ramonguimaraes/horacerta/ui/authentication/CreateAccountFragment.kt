package com.ramonguimaraes.horacerta.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ramonguimaraes.horacerta.databinding.FragmentCreateAccountBinding
import com.ramonguimaraes.horacerta.presenter.CreateAccountViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateAccountFragment : Fragment() {

    private val viewModel: CreateAccountViewModel by viewModel()
    private val binding: FragmentCreateAccountBinding by lazy {
        FragmentCreateAccountBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }
}
