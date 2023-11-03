package com.ramonguimaraes.horacerta.presenter.cashFlow.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.CashRegisterItemV2Binding
import com.ramonguimaraes.horacerta.domain.cashFlow.CashRegister
import com.ramonguimaraes.horacerta.presenter.cashFlow.model.PaymentMethod
import com.ramonguimaraes.horacerta.presenter.viewUtils.DefaultDiffCallback
import com.ramonguimaraes.horacerta.utils.toCurrency
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CashRegisterAdapter :
    ListAdapter<CashRegister, CashRegisterAdapter.ViewHolder>(DefaultDiffCallback<CashRegister>()),
    Filterable {
    private var originalList: List<CashRegister> = emptyList()
    private var filteredList: List<CashRegister> = emptyList()

    class ViewHolder(val binding: CashRegisterItemV2Binding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(item: CashRegister) {
            binding.imgPaymentMethod.setImageDrawable(
                context.resources.getDrawable(
                    resolvePaymentImage(item.paymentMethod).first
                )
            )

            val resourcePair = resolvePaymentImage(item.paymentMethod)


            val image = resourcePair.first
            val color = resourcePair.second









            binding.txtTotal.text = item.total.toCurrency()
            binding.txtDate.text = formatDate(item.date.time)
            binding.txtClientName.text = item.clientName
            binding.txtServicesList.text = item.getServices()
        }

        private fun formatDate(date: Date): String {
            val format = SimpleDateFormat("dd/MM/yy - hh/mm", Locale("pt", "BR"))
            return format.format(date).toString()
        }

        private fun resolvePaymentImage(paymentMethod: PaymentMethod): Pair<Int, Int> {
            return when (paymentMethod) {
                PaymentMethod.PIX -> Pair(R.drawable.ic_pix, R.color.teal_700)
                PaymentMethod.CASH -> Pair(R.drawable.ic_cash, R.color.green)
                PaymentMethod.CARD -> Pair(R.drawable.ic_card, R.color.purple_500)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CashRegisterItemV2Binding.inflate(inflater, parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setList(companyList: List<CashRegister>) {
        originalList = companyList
        filteredList = companyList
        submitList(originalList)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                filteredList = if (charSequence.isNullOrEmpty()) {
                    originalList
                } else {

                    originalList.filter {
                        charSequence.contains(it.paymentMethod.toString())
                    }
                }

                return FilterResults().apply {
                    values = filteredList
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults
            ) {
                val values = if (charSequence.isNullOrEmpty()) {
                    originalList
                } else {
                    filterResults.values as MutableList<CashRegister>
                }
                submitList(values)
            }
        }
    }
}
