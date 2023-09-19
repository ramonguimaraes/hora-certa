package com.ramonguimaraes.horacerta.ui.services

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ramonguimaraes.horacerta.databinding.ServiceRegistrationItemLayoutBinding
import com.ramonguimaraes.horacerta.domain.services.model.Service
import com.ramonguimaraes.horacerta.utils.DefaultDiffCallback

class ServicesAdapter :
    ListAdapter<Service, ServicesAdapter.ServiceViewHolder>(DefaultDiffCallback()) {

    private var onClick: (service: Service) -> Unit = {}
    fun setOnClick(onClick: (Service) -> Unit) {
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
        fun bind(service: Service) {
            binding.txtPrice.text = service.price.toString()
            binding.txtTitle.text = service.title
            binding.txtEstimatedDuration.text = service.estimatedDuration.toString()
            binding.root.setOnClickListener { onClick.invoke(service) }
        }
    }
}
