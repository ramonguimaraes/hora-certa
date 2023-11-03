package com.ramonguimaraes.horacerta.presenter.cashFlow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ramonguimaraes.horacerta.databinding.FragmentCashFlowBinding
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.presenter.cashFlow.model.PaymentMethod
import com.ramonguimaraes.horacerta.presenter.cashFlow.ui.adapter.CashRegisterAdapter
import com.ramonguimaraes.horacerta.presenter.cashFlow.viewModel.CashFlowViewModel
import com.ramonguimaraes.horacerta.presenter.cashFlow.viewModel.Period
import org.koin.androidx.viewmodel.ext.android.viewModel

class CashFlowFragment : Fragment() {

    private val viewModel: CashFlowViewModel by viewModel()
    private val binding: FragmentCashFlowBinding by lazy {
        FragmentCashFlowBinding.inflate(layoutInflater)
    }
    private val cashRegisterAdapter = CashRegisterAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.rvCashRegister.adapter = cashRegisterAdapter
        binding.rvCashRegister.layoutManager = LinearLayoutManager(context)
        binding.backButton.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        viewModel.paymentMethod.observe(viewLifecycleOwner) {
            cashRegisterAdapter.filter.filter(it)
        }

        viewModel.cashRegisters.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    Toast.makeText(requireContext(), "Carregando lista", Toast.LENGTH_SHORT).show()
                }

                is Resource.Success -> {
                    cashRegisterAdapter.setList(it.result)
                }

                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Deu erro", Toast.LENGTH_SHORT).show()
                }
            }
        }

        setPeriodSpinner()
        setCashPaymentMethod()
        setPixPaymentMethod()
        setCardPaymentMethod()
        return binding.root
    }

    private fun setCashPaymentMethod() {
        binding.checkBoxCash.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setPaymentMethodFilter(PaymentMethod.CASH, isChecked)
        }
    }

    private fun setPixPaymentMethod() {
        binding.checkBoxPix.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setPaymentMethodFilter(PaymentMethod.PIX, isChecked)
        }
    }

    private fun setCardPaymentMethod() {
        binding.checkBoxCard.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setPaymentMethodFilter(PaymentMethod.CARD, isChecked)
        }
    }

    private fun setPeriodSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            viewModel.periods
        )

        binding.spinnerPeriod.adapter = adapter
        binding.spinnerPeriod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                binding.checkBoxCash.isChecked = false
                binding.checkBoxPix.isChecked = false
                binding.checkBoxCard.isChecked = false
                val selected = binding.spinnerPeriod.selectedItem as Period
                viewModel.loadByPeriod(selected)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
}
