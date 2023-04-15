package com.ramonguimaraes.horacerta

import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module

fun authModule() = module {
    single { FirebaseAuth.getInstance() }
    factory<AuthRepository> { AuthRepositoryImpl(get()) }
}
