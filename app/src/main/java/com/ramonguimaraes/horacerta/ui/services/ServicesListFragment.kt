package com.ramonguimaraes.horacerta.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ramonguimaraes.horacerta.databinding.FragmentServicesListBinding

class ServicesListFragment : Fragment() {

    private val binding: FragmentServicesListBinding by lazy {
        FragmentServicesListBinding.inflate(layoutInflater)
    }
    private val servicesAdapter: ServicesAdapter = ServicesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        configureRecycler()
        return binding.root
    }

    private fun configureRecycler() {
        binding.rvServices.apply {
            adapter = servicesAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}
