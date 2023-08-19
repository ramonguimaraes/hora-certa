package com.ramonguimaraes.horacerta.ui.clientHome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ramonguimaraes.horacerta.R

/**
 * A simple [Fragment] subclass.
 * Use the [ClientHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClientHomeFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_home, container, false)
    }
}