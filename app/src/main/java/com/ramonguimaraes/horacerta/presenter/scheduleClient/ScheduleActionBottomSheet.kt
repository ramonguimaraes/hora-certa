package com.ramonguimaraes.horacerta.presenter.scheduleClient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ramonguimaraes.horacerta.databinding.BottomSheetScheduleActionBinding
import com.ramonguimaraes.horacerta.presenter.viewUtils.extensions.visible

class ScheduleActionBottomSheet(
    private val showConfirm: Boolean = false,
    private val showCancel: Boolean = true,
    private val showReschedule: Boolean = true,
    private val cancel: () -> Unit,
    private val reschedule: () -> Unit,
    private val confirm: () -> Unit = {},
) : BottomSheetDialogFragment() {

    private val binding: BottomSheetScheduleActionBinding by lazy {
        BottomSheetScheduleActionBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if (showConfirm) binding.confirmButton.visible()
        binding.confirmButton.setOnClickListener {
            confirm.invoke()
            dismiss()
        }

        if (showCancel) binding.cancelButton.visible()
        binding.cancelButton.setOnClickListener {
            cancel.invoke()
            dismiss()
        }

        if (showReschedule) binding.rescheduleButton.visible()
        binding.rescheduleButton.setOnClickListener {
            reschedule.invoke()
            dismiss()
        }

        return binding.root
    }
}
