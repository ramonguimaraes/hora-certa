package com.ramonguimaraes.horacerta.presenter.schedule.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ramonguimaraes.horacerta.databinding.ServicesItemLayoutBinding
import com.ramonguimaraes.horacerta.domain.services.model.Service
import com.ramonguimaraes.horacerta.presenter.viewUtils.DefaultDiffCallback

class ServicesAdapter :
    ListAdapter<Service, ServicesViewHolder>(DefaultDiffCallback<Service>()) {

    private var checkListener: (item: Service, isChecked: Boolean) -> Unit = { _: Service, _: Boolean -> }
    fun setCheckListener(listener: (item: Service, isChecked: Boolean) -> Unit) {
        checkListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ServicesItemLayoutBinding.inflate(layoutInflater, parent, false)
        return ServicesViewHolder(binding, checkListener)
    }

    override fun onBindViewHolder(holder: ServicesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ServicesViewHolder(
    private val binding: ServicesItemLayoutBinding,
    private val checkListener: (item: Service, isChecked: Boolean) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(service: Service) {
        binding.cbService.apply {
            text = service.title
            isChecked = service.checked
        }

        binding.cbService.setOnCheckedChangeListener { compoundButton, _ ->
            service.checked = compoundButton.isChecked
            checkListener.invoke(service, compoundButton.isChecked)
        }
    }
}
