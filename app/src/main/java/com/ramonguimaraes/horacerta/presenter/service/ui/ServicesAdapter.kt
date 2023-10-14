package com.ramonguimaraes.horacerta.presenter.service.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ramonguimaraes.horacerta.databinding.ServiceRegistrationItemLayoutBinding
import com.ramonguimaraes.horacerta.presenter.service.model.ServiceView
import com.ramonguimaraes.horacerta.presenter.viewUtils.DefaultDiffCallback

class ServicesAdapter :
    ListAdapter<ServiceView, ServicesAdapter.ServiceViewHolder>(DefaultDiffCallback()) {

    private var onClick: (service: ServiceView) -> Unit = {}
    fun setOnClick(onClick: (ServiceView) -> Unit) {
        this.onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ServiceRegistrationItemLayoutBinding.inflate(layoutInflater, parent, false)
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ServiceViewHolder(private val binding: ServiceRegistrationItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(service: ServiceView) {
            binding.txtPrice.text = service.price.toString()
            binding.txtTitle.text = service.title
            binding.txtEstimatedDuration.text = service.duration.toString()
            binding.root.setOnClickListener { onClick.invoke(service) }
        }
    }
}
