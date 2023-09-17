package com.ramonguimaraes.horacerta.ui.scheduleConfig

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ramonguimaraes.horacerta.databinding.FragmentScheduleConfigBinding

class ScheduleConfigFragment : Fragment() {

    val binding: FragmentScheduleConfigBinding by lazy {
        FragmentScheduleConfigBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.fabAddConfig.setOnClickListener {
            showBottomSheet()
        }
        return binding.root
    }

    private fun showBottomSheet() {
        ScheduleConfigBottomSheetDialog()
            .show(parentFragmentManager, "scheduleConfigBottomSheet")
    }
}
