package com.ramonguimaraes.horacerta.domain.user.model

data class User(
    val uid: String = "",
    val name:  String = "",
    val email: String = "",
    val accountType: AccountType = AccountType.NONE,
    val deviceToken: String = ""
)

fun User.toHashMap(): HashMap<String, Any> {
    return hashMapOf(
        "uid" to uid,
        "name" to name,
        "email" to email,
        "accountType" to accountType,
        "deviceToken" to deviceToken
    )
}
