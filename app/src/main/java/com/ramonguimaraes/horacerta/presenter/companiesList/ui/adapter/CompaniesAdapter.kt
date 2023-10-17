package com.ramonguimaraes.horacerta.presenter.companiesList.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.CompanyProfileItemBinding
import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.domain.services.model.Service
import com.ramonguimaraes.horacerta.presenter.viewUtils.DefaultDiffCallback
import com.squareup.picasso.Picasso

class CompaniesAdapter :
    ListAdapter<CompanyProfile, CompaniesAdapter.CompanyViewHolder>(DefaultDiffCallback<CompanyProfile>()),
    Filterable {

    private var originalList: List<CompanyProfile> = emptyList()
    private var filteredList: List<CompanyProfile> = emptyList()

    private var clickListener: (item: CompanyProfile) -> Unit = {}
    fun setClickListener(listener: (item: CompanyProfile) -> Unit) {
        clickListener = listener
    }

    inner class CompanyViewHolder(
        private val binding: CompanyProfileItemBinding,
        private val checkListener: (item: CompanyProfile) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(companyProfile: CompanyProfile) {
            binding.txtCompanyName.text = companyProfile.companyName
            binding.txtCompanySegment.text = companyProfile.companySegment
            Picasso.get()
                .load(companyProfile.photoUri)
                .placeholder(R.drawable.loading)
                .error(R.drawable.user_default)
                .into(binding.imgProfile)

            binding.root.setOnClickListener { checkListener.invoke(companyProfile) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CompanyProfileItemBinding.inflate(layoutInflater, parent, false)
        return CompanyViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setCompanyList(companyList: List<CompanyProfile>) {
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
                        it.companyName.contains(
                            charSequence,
                            ignoreCase = false
                        ) || it.companySegment.contains(charSequence, ignoreCase = false)
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
                    filterResults.values as MutableList<CompanyProfile>
                }
                submitList(values)
            }
        }
    }
}
