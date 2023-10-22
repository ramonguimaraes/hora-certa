package com.ramonguimaraes.horacerta.presenter.scheduleClient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ramonguimaraes.horacerta.databinding.BottomSheetScheduleActionBinding

class ScheduleActionBottomSheet(
    private val cancel: () -> Unit,
    private val reschedule: () -> Unit,
) : BottomSheetDialogFragment() {

    private val binding: BottomSheetScheduleActionBinding by lazy {
        BottomSheetScheduleActionBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.cancelButton.setOnClickListener {
            cancel.invoke()
            dismiss()
        }

        binding.rescheduleButton.setOnClickListener {
            reschedule.invoke()
            dismiss()
        }

        return binding.root
    }
}
