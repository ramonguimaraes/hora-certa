package com.ramonguimaraes.horacerta.domain.cashFlow

import com.google.firebase.Timestamp
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ramonguimaraes.horacerta.domain.schedule.model.toTimeStamp
import com.ramonguimaraes.horacerta.domain.services.model.Service
import com.ramonguimaraes.horacerta.presenter.cashFlow.model.PaymentMethod
import com.ramonguimaraes.horacerta.utils.toCurrency
import java.util.Calendar

data class CashRegister(
    val companyUid: String = "",
    val total: Double = 0.0,
    val clientName: String = "",
    val date: Calendar = Calendar.getInstance(),
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val services: List<Service> = listOf()
) {

    fun getServices(): String {
        if (services.isNotEmpty()) {
            val servicesMap = services.map {
                Pair(it.title, it.price)
            }
            var list = ""
            servicesMap.forEach { pair ->
                list += "${pair.first.replaceFirstChar(Char::titlecase)} - ${pair.second.toCurrency()}\n"
            }
            return list
        }
        return ""
    }
}

fun CashRegister.toHashMap(): HashMap<String, Any> {
    return hashMapOf(
        "companyUid" to companyUid,
        "total" to total,
        "clientName" to clientName,
        "date" to date.toTimeStamp(),
        "paymentMethod" to paymentMethod,
        "services" to services.map { it.id }
    )
}

fun QueryDocumentSnapshot.toCashRegister(): CashRegister {
    val date = get("date", Timestamp::class.java)
    val calendar = Calendar.getInstance()
    calendar.time = date?.toDate()!!

    return CashRegister(
        companyUid = get("companyUid", String::class.java) ?: "",
        total = get("total", Double::class.java) ?: 0.0,
        clientName = get("clientName", String::class.java) ?: "",
        paymentMethod = get("paymentMethod", PaymentMethod::class.java) ?: PaymentMethod.PIX,
        date = calendar
    )
}
