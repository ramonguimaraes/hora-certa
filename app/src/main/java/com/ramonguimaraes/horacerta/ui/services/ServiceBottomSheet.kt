package com.ramonguimaraes.horacerta.ui.services

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ramonguimaraes.horacerta.databinding.BottomSheetServiceLayoutBinding
import com.ramonguimaraes.horacerta.presenter.service.ServiceViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ServiceBottomSheet : BottomSheetDialogFragment() {

    private val viewModel: ServiceViewModel by viewModel()
    private val binding: BottomSheetServiceLayoutBinding by lazy {
        BottomSheetServiceLayoutBinding.inflate(layoutInflater)
    }
    private var onDismissListener: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        configureDataBinding()
        dismissObverse()
        return binding.root
    }

    private fun configureDataBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun dismissObverse() {
        viewModel.dismiss.observe(viewLifecycleOwner) {
            if (it) dismiss()
        }
    }

    fun setOnDismissListener(listener: () -> Unit): ServiceBottomSheet {
        onDismissListener = listener
        return this
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener.invoke()
    }
}
