package com.ramonguimaraes.horacerta.ui.schedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ramonguimaraes.horacerta.databinding.ScheduleItemLayoutBinding
import com.ramonguimaraes.horacerta.domain.schedule.model.toTimeStamp
import com.ramonguimaraes.horacerta.utils.DefaultDiffCallback
import java.time.DayOfWeek
import java.util.Calendar
import java.util.Date


class ScheduleAdapter: ListAdapter<Appointment, ScheduleAdapter.ScheduleViewHolder>(DefaultDiffCallback<Appointment>()) {

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

data class Appointment(
    val time: String = "",
    val client: String = "",
    val service: String = "",
    val companyUid: String = "",
    val date: Calendar
)

fun Appointment.toHashMap(): HashMap<String, Any> {
    return hashMapOf(
        "time" to time,
        "client" to client,
        "service" to service,
        "companyUid" to companyUid,
        "date" to date.onlyDate().toTimeStamp()
    )
}

fun Calendar.onlyDate(): Calendar {
    val calendar = this
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar
}
