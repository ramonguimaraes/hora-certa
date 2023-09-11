package com.ramonguimaraes.horacerta.ui.scheduleConfig

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ramonguimaraes.horacerta.databinding.ScheduleConfigItemLayoutBinding
import com.ramonguimaraes.horacerta.domain.scheduleConfig.model.ScheduleConfig
import com.ramonguimaraes.horacerta.utils.DayOfWeek.FRIDAY
import com.ramonguimaraes.horacerta.utils.DayOfWeek.MONDAY
import com.ramonguimaraes.horacerta.utils.DayOfWeek.SATURDAY
import com.ramonguimaraes.horacerta.utils.DayOfWeek.SUNDAY
import com.ramonguimaraes.horacerta.utils.DayOfWeek.THURSDAY
import com.ramonguimaraes.horacerta.utils.DayOfWeek.TUESDAY
import com.ramonguimaraes.horacerta.utils.DayOfWeek.WEDNESDAY
import com.ramonguimaraes.horacerta.utils.DefaultDiffCallback

class ScheduleConfigAdapter : ListAdapter<ScheduleConfig, ScheduleConfigAdapter.ViewHolder>(DefaultDiffCallback<ScheduleConfig>()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ScheduleConfigItemLayoutBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ScheduleConfigItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(scheduleConfig: ScheduleConfig) {
            binding.txtDayOfWeek.text = when (scheduleConfig.dayOfWeek) {
                SUNDAY -> "Domingo"
                MONDAY -> "Segunda"
                TUESDAY -> "Terça"
                WEDNESDAY -> "Quarta"
                THURSDAY -> "Quinta"
                FRIDAY -> "Sexta"
                SATURDAY -> "Sabado"
            }

            binding.txtMoningTime.text =
                "${scheduleConfig.openTime} até ${scheduleConfig.intervalStart}"
            binding.txtAfternoonTime.text =
                "${scheduleConfig.intervalEnd} até ${scheduleConfig.closeTime}"
        }
    }
}