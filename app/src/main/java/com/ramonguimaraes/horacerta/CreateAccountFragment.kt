package com.ramonguimaraes.horacerta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ramonguimaraes.horacerta.databinding.FragmentCreateAccountBinding

class CreateAccountFragment : Fragment() {

    private val binding: FragmentCreateAccountBinding by lazy {
        FragmentCreateAccountBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
}
