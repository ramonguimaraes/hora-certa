package com.ramonguimaraes.horacerta.presenter.scheduleClient

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.api.Distribution.BucketOptions.Linear
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.ClientScheduleItemBinding
import com.ramonguimaraes.horacerta.domain.schedule.model.toTimeStamp
import com.ramonguimaraes.horacerta.domain.services.model.Service
import com.ramonguimaraes.horacerta.generated.callback.OnClickListener
import com.ramonguimaraes.horacerta.presenter.viewUtils.DefaultDiffCallback
import com.ramonguimaraes.horacerta.presenter.viewUtils.extensions.gone
import com.ramonguimaraes.horacerta.presenter.viewUtils.extensions.visible
import com.ramonguimaraes.horacerta.utils.extensions.formattedDate
import com.squareup.picasso.Picasso
import java.util.Calendar

class ClientScheduleAdapter :
    ListAdapter<ClientAppointment, ClientScheduleAdapter.ClientScheduleViewHolder>(
        DefaultDiffCallback<ClientAppointment>()
    ) {

    private var clickListener: (item: ClientAppointment) -> Unit = {}
    fun setClickListener(listener: (item: ClientAppointment) -> Unit) {
        clickListener = listener
    }

    private var phoneClick: (item: ClientAppointment) -> Unit = {}
    fun setPhoneClick(listener: (item: ClientAppointment) -> Unit) {
        phoneClick = listener
    }

    private var addressClick: (item: ClientAppointment) -> Unit = {}
    fun setAddressClick(listener: (item: ClientAppointment) -> Unit) {
        addressClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientScheduleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ClientScheduleItemBinding.inflate(layoutInflater, parent, false)
        return ClientScheduleViewHolder(
            binding,
            parent.context,
            clickListener,
            phoneClick,
            addressClick
        )
    }

    override fun onBindViewHolder(holder: ClientScheduleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ClientScheduleViewHolder(
        private val binding: ClientScheduleItemBinding,
        private val context: Context,
        private val clickListener: (item: ClientAppointment) -> Unit,
        private val phoneClick: (item: ClientAppointment) -> Unit,
        private val addressClick: (item: ClientAppointment) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ClientAppointment) {

            if (item.latitude == null || item.longitude == null) {
                binding.btnAddress.gone()
            }

            if (item.showDateLabel) {
                binding.dateLabel.text = item.date.formattedDate()
                binding.dateLabel.visible()
            }

            binding.servicesContainer.removeAllViews()
            item.services.forEach { service ->
                binding.servicesContainer.addView(getServiceView(service, context))
            }

            binding.txtCompanyName.text = item.companyName
            binding.txtTime.text = item.getHourString()

            Picasso.get().load(item.photoUri).placeholder(R.drawable.loading)
                .error(R.drawable.user_default).into(binding.imgProfile)

            binding.btnPhone.setOnClickListener {
                phoneClick.invoke(item)
            }

            binding.btnAddress.setOnClickListener {
                addressClick.invoke(item)
            }

            binding.root.setOnClickListener { clickListener.invoke(item) }
        }

        private fun getServiceView(service: Service, context: Context): LinearLayout {
            val linearLayout = LinearLayout(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                )
                orientation = LinearLayout.HORIZONTAL
            }

            val textViewLayoutParams = LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )

            val textViewTitle = TextView(context).apply {
                text = service.title
                layoutParams = textViewLayoutParams
            }

            linearLayout.addView(textViewTitle)

            return linearLayout
        }
    }
}