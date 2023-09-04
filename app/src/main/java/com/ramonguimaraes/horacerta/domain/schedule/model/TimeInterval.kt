package com.ramonguimaraes.horacerta.domain.schedule.model

import java.time.LocalTime

class TimeInterval(
    val time: LocalTime,
    var disponivel: Boolean = true,
    var show: Boolean = true
)
