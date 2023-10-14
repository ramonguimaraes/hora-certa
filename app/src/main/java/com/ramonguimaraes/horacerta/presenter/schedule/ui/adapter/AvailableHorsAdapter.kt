package com.ramonguimaraes.horacerta.presenter.schedule.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ramonguimaraes.horacerta.databinding.AvailableHoursItemLayoutBinding
import com.ramonguimaraes.horacerta.domain.schedule.model.TimeInterval
import com.ramonguimaraes.horacerta.presenter.viewUtils.DefaultDiffCallback

class AvailableHorsAdapter :
    ListAdapter<TimeInterval, AvailableHorsViewHolder>(DefaultDiffCallback<TimeInterval>()) {

    private var listener: (item: TimeInterval) -> Unit = {}
    fun setOnClick(listener: (item: TimeInterval) -> Unit) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableHorsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AvailableHoursItemLayoutBinding.inflate(layoutInflater, parent, false)
        return AvailableHorsViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: AvailableHorsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: MutableList<TimeInterval>?) {
        super.submitList(list?.filter { it.show })
    }
}

class AvailableHorsViewHolder(
    private val binding: AvailableHoursItemLayoutBinding,
    private val listener: (item: TimeInterval) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: TimeInterval) {
        binding.txtAvailableHour.text = item.time.toString()
        binding.txtAvailableHour.setOnClickListener { listener.invoke(item) }
    }
}
