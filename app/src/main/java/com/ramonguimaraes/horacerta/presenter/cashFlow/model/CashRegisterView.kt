package com.ramonguimaraes.horacerta.presenter.cashFlow.model

import java.util.Calendar

data class CashRegisterView(
    val total: Double = 0.0,
    val clientName: String = "",
    val date: Calendar = Calendar.getInstance(),
    val paymentMethod: PaymentMethod = PaymentMethod.CASH
)
