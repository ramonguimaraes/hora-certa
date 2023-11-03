package com.ramonguimaraes.horacerta.presenter.schedule.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ramonguimaraes.horacerta.databinding.ScheduleItemLayoutV2Binding
import com.ramonguimaraes.horacerta.domain.schedule.model.Appointment
import com.ramonguimaraes.horacerta.presenter.viewUtils.DefaultDiffCallback
import com.ramonguimaraes.horacerta.presenter.viewUtils.extensions.gone
import com.ramonguimaraes.horacerta.presenter.viewUtils.extensions.visible
import java.util.Calendar

class ScheduleAdapter : ListAdapter<Appointment, ScheduleAdapter.ScheduleViewHolder>(
    DefaultDiffCallback<Appointment>()
) {

    private var confirmListener: (item: Appointment) -> Unit = {}
    fun setConfirmListener(listener: (item: Appointment) -> Unit) {
        confirmListener = listener
    }

    class ScheduleViewHolder(
        private val binding: ScheduleItemLayoutV2Binding,
        private val confirmListener: (Appointment) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(appointment: Appointment) {
            binding.txtTime.text = appointment.getHourString()
            binding.txtClient.text = appointment.clientName
            binding.txtService.text = appointment.getServices()
            binding.root.setOnClickListener {
                confirmListener.invoke(appointment)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ScheduleItemLayoutV2Binding.inflate(layoutInflater, parent, false)
        return ScheduleViewHolder(binding, confirmListener)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
