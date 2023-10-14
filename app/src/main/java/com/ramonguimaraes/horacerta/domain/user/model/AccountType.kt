package com.ramonguimaraes.horacerta.domain.user.model

enum class AccountType(val type: Int) {
    NONE(type = 0),
    CLIENT(type = 1),
    COMPANY(type = 2)
}
