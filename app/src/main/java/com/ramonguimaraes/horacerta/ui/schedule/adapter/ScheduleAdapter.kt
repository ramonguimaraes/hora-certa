package com.ramonguimaraes.horacerta.ui.schedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ramonguimaraes.horacerta.databinding.ScheduleItemLayoutBinding
import com.ramonguimaraes.horacerta.domain.schedule.model.Appointment
import com.ramonguimaraes.horacerta.utils.DefaultDiffCallback

class ScheduleAdapter :
    ListAdapter<Appointment, ScheduleAdapter.ScheduleViewHolder>(DefaultDiffCallback<Appointment>()) {

    class ScheduleViewHolder(val binding: ScheduleItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(appointment: Appointment) {
            binding.txtTime.text = appointment.time
            binding.txtClient.text = appointment.client
            binding.txtService.text = appointment.service
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ScheduleItemLayoutBinding =
            ScheduleItemLayoutBinding.inflate(layoutInflater, parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
