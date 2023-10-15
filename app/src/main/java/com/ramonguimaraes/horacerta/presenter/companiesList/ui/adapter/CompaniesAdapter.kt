package com.ramonguimaraes.horacerta.presenter.companiesList.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.CompanyProfileItemBinding
import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.presenter.viewUtils.DefaultDiffCallback
import com.squareup.picasso.Picasso

class CompaniesAdapter :
    ListAdapter<CompanyProfile, CompanyViewHolder>(DefaultDiffCallback<CompanyProfile>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CompanyProfileItemBinding.inflate(layoutInflater, parent, false)
        return CompanyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CompanyViewHolder(private val binding: CompanyProfileItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(companyProfile: CompanyProfile) {
        binding.txtCompanyName.text = companyProfile.companyName
        binding.txtCompanySegment.text = companyProfile.companySegment
        Picasso.get()
            .load(companyProfile.photoUri)
            .placeholder(R.drawable.loading)
            .error(R.drawable.user_default)
            .into(binding.imgProfile)
    }
}