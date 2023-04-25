package com.ramonguimaraes.horacerta.ui.companyProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ramonguimaraes.horacerta.databinding.FragmentCompanyProfileBinding

class CompanyProfileFragment : Fragment() {

    private val binding: FragmentCompanyProfileBinding by lazy {
        FragmentCompanyProfileBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
}
