package com.ramonguimaraes.horacerta.presenter.scheduleClient

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ramonguimaraes.horacerta.databinding.BottomSheetPhoneActionBinding


class PhoneActionBottomSheet(
    private val phone: String
) : BottomSheetDialogFragment() {
    private val binding: BottomSheetPhoneActionBinding by lazy {
        BottomSheetPhoneActionBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.callButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phone")
            startActivity(intent)
            dismiss()
        }

        binding.whatsAppButton.setOnClickListener {
            try {
                val pm = context?.packageManager
                pm?.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)

                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://wa.me/$phone")
                startActivity(intent)
                dismiss()
            } catch (e: PackageManager.NameNotFoundException) {
                Toast.makeText(
                    context,
                    "Você precisa ter o WhatsApp instalado",
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        }

        return binding.root
    }
}