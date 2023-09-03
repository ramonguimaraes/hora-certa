package com.ramonguimaraes.horacerta.ui.schedule

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.AvailableHoursItemLayoutBinding
import com.ramonguimaraes.horacerta.databinding.FragmentScheduleRegistrationBinding
import com.ramonguimaraes.horacerta.databinding.ServicesItemLayoutBinding
import com.ramonguimaraes.horacerta.presenter.schedule.ScheduleRegistrationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Calendar
import java.util.Locale
/**
class ScheduleRegistrationFragmentOld : Fragment() {

    private val mBinding: FragmentScheduleRegistrationBinding by lazy {
        FragmentScheduleRegistrationBinding.inflate(layoutInflater)
    }

    private val viewModel: ScheduleRegistrationViewModel by viewModel()

    val db = FirebaseFirestore.getInstance()
    private var calendar: Calendar = Calendar.getInstance()
    private var hours: List<TimeInterval> = listOf(
        TimeInterval(LocalTime.of(8, 0)),
        TimeInterval(LocalTime.of(8, 30)),
        TimeInterval(LocalTime.of(9, 0)),
        TimeInterval(LocalTime.of(9, 30)),
        TimeInterval(LocalTime.of(10, 0)),
        TimeInterval(LocalTime.of(10, 30)),
        TimeInterval(LocalTime.of(11, 0)),
        TimeInterval(LocalTime.of(11, 30), false),
        TimeInterval(LocalTime.of(12, 0), false),
        TimeInterval(LocalTime.of(12, 30), false),
        TimeInterval(LocalTime.of(14, 0)),
        TimeInterval(LocalTime.of(14, 30)),
        TimeInterval(LocalTime.of(15, 0)),
        TimeInterval(LocalTime.of(15, 30)),
        TimeInterval(LocalTime.of(16, 0)),
        TimeInterval(LocalTime.of(16, 30)),
        TimeInterval(LocalTime.of(17, 0)),
        TimeInterval(LocalTime.of(17, 30)),
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runBlocking {
            withContext(Dispatchers.IO) {
               val a= db.collection("horarios").get().await()
                Log.d("RESULTADO", a.toString())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val adapter = AvailableHorsAdapter(
            hours, object : AvailableHorsAdapter.OnClick {
                override fun onClick(item: TimeInterval) {
                    Log.d("DATE", calendar.time.toString())
                    Log.d("HORA", item.time.toString())


                    calendar.set(Calendar.HOUR_OF_DAY, item.time.hour)
                    calendar.set(Calendar.MINUTE, item.time.minute)
                    calendar.set(Calendar.SECOND, 0)

                    Log.d("CALENDAR", calendar.time.toString())

                }
            }
        )
        mBinding.rvAvailableHours.adapter = adapter
        mBinding.rvAvailableHours.layoutManager = GridLayoutManager(context, 3)

        mBinding.rvServices.adapter = ServicesAdapter(
            listOf(
                ServiceItem("Corte de cabelo", false, 60),
                ServiceItem("Barba", false, 90),
                ServiceItem("Pintura", false, 30),
                ServiceItem("Corte personalizado", false, 30),
                ServiceItem("Sombrancelha", false, 200)
            ), object : ServicesAdapter.OnServiceChecked {
                override fun onChecked(checkedServices: MutableList<ServiceItem>) {
                    adapter.updateList(configuraListaDeHoras(checkedServices))
                    adapter.notifyDataSetChanged()
                }
            }
        )


        mBinding.btnOpenCalendar.setOnClickListener {
            val year = Calendar.getInstance().get(Calendar.YEAR)
            val month = Calendar.getInstance().get(Calendar.MONTH)
            val dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->

                    calendar.set(year, month, dayOfMonth)

                    val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
                        Calendar.SUNDAY -> "Domingo"
                        Calendar.MONDAY -> "Segunda"
                        Calendar.TUESDAY -> "Terça"
                        Calendar.WEDNESDAY -> "Quarta"
                        Calendar.THURSDAY -> "Quinta"
                        Calendar.FRIDAY -> "Sexta"
                        else -> "Sábado"
                    }

                    val format = SimpleDateFormat("MMM", Locale.forLanguageTag("PT-BR"))
                    val formattedMonth = format.format(calendar.time)
                        .subSequence(0, 3)
                        .toString()
                        .replaceFirstChar { it.uppercase() }

                    val formattedDate = resources.getString(
                        R.string.date_temaplate,
                        dayOfWeek,
                        dayOfMonth,
                        formattedMonth,
                        year
                    )
                    mBinding.txtSelectedDate.text = formattedDate
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    Log.d("CALENDAR", calendar.time.toString())

                    viewModel.load(calendar)
                },
                year, month, dayOfMonth
            ).show()
        }

        mBinding.btnMark.setOnClickListener {

            runBlocking {
                withContext(Dispatchers.IO) {
                    val obj = hashMapOf<String, Timestamp>()
                    obj.set("horario", Timestamp(calendar.time))
                    db.collection("horarios").add(obj).await()
                }
            }
        }

        return mBinding.root
    }

    private fun configuraListaDeHoras(checkedServices: MutableList<ServiceItem>): List<TimeInterval> {
        val total = checkedServices.sumOf { it.duration }
        val disponiveis: MutableList<TimeInterval> = mutableListOf()
        val qtdIntervalos = total / 30

        for (i in 0 until hours.size - (qtdIntervalos - 1)) {
            val subLista = hours.subList(i, i + qtdIntervalos)
            if (subLista.all { it.disponivel }) {
                disponiveis.add(hours[i])
            }
        }

        return disponiveis
    }
}

class ServiceItem(
    val title: String, val checked: Boolean, val duration: Int
)

class ServicesAdapter(
    private val services: List<ServiceItem>,
    private val callback: OnServiceChecked
) :
    RecyclerView.Adapter<ServicesAdapter.ServicesViewHolder>() {

    private val checkedServices: MutableList<ServiceItem> = mutableListOf()

    inner class ServicesViewHolder(private val binding: ServicesItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(service: ServiceItem) {
            binding.cbService.apply {
                text = service.title
                isChecked = service.checked
            }

            binding.cbService.setOnCheckedChangeListener { compoundButton, b ->
                if (compoundButton.isChecked) {
                    checkedServices.add(service)
                } else {
                    checkedServices.remove(service)
                }

                Log.d("TESTE", checkedServices.map { it.title }.toString())
                callback.onChecked(checkedServices)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ServicesItemLayoutBinding.inflate(layoutInflater, parent, false)
        return ServicesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return services.size
    }

    override fun onBindViewHolder(holder: ServicesViewHolder, position: Int) {
        holder.bind(services[position])
    }

    interface OnServiceChecked {
        fun onChecked(checkedServices: MutableList<ServiceItem>)
    }
}

class TimeInterval(
    val time: LocalTime,
    var disponivel: Boolean = true
)

class AvailableHorsAdapter(
    private var availableHorsList: List<TimeInterval>,
    private val callback: OnClick?
) : RecyclerView.Adapter<AvailableHorsAdapter.AvailableHorsViewHolder>() {

    inner class AvailableHorsViewHolder(private val binding: AvailableHoursItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TimeInterval) {
            if (item.disponivel) {
                binding.txtAvailableHour.text = item.time.toString()
                binding.txtAvailableHour.setOnClickListener { callback?.onClick(item) }
            } else {
                binding.txtAvailableHour.text = ""
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableHorsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AvailableHoursItemLayoutBinding.inflate(layoutInflater, parent, false)
        return AvailableHorsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return availableHorsList.size
    }

    override fun onBindViewHolder(holder: AvailableHorsViewHolder, position: Int) {
        holder.bind(availableHorsList[position])
    }

    fun updateList(configuraListaDeHoras: List<TimeInterval>) {
        availableHorsList = configuraListaDeHoras
        notifyDataSetChanged()
    }

    interface OnClick {
        fun onClick(item: TimeInterval)
    }
}
**/