package com.ramonguimaraes.horacerta.di.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

fun firebaseModule() = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
}
