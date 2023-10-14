package com.ramonguimaraes.horacerta.presenter.scheduleConfig.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ramonguimaraes.horacerta.databinding.ScheduleConfigItemLayoutBinding
import com.ramonguimaraes.horacerta.domain.scheduleConfig.model.ScheduleConfig
import com.ramonguimaraes.horacerta.domain.schedule.model.DayOfWeek.FRIDAY
import com.ramonguimaraes.horacerta.domain.schedule.model.DayOfWeek.MONDAY
import com.ramonguimaraes.horacerta.domain.schedule.model.DayOfWeek.SATURDAY
import com.ramonguimaraes.horacerta.domain.schedule.model.DayOfWeek.SUNDAY
import com.ramonguimaraes.horacerta.domain.schedule.model.DayOfWeek.THURSDAY
import com.ramonguimaraes.horacerta.domain.schedule.model.DayOfWeek.TUESDAY
import com.ramonguimaraes.horacerta.domain.schedule.model.DayOfWeek.WEDNESDAY
import com.ramonguimaraes.horacerta.presenter.viewUtils.DefaultDiffCallback

class ScheduleConfigAdapter :
    ListAdapter<ScheduleConfig, ScheduleConfigAdapter.ViewHolder>(DefaultDiffCallback<ScheduleConfig>()) {

    private var onClick: (scheduleConfig: ScheduleConfig) -> Unit = {}
    fun setOnClick(onClick: (ScheduleConfig) -> Unit) {
        this.onClick = onClick
    }

    override fun submitList(list: MutableList<ScheduleConfig>?) {
        super.submitList(list)
        notifyDataSetChanged()
    }

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

    inner class ViewHolder(private val binding: ScheduleConfigItemLayoutBinding) :
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

            binding.root.setOnClickListener {
                onClick.invoke(scheduleConfig)
            }
        }
    }
}
