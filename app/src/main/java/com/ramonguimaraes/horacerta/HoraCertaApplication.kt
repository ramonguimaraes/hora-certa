package com.ramonguimaraes.horacerta

import android.app.Application
import com.ramonguimaraes.horacerta.di.KoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HoraCertaApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@HoraCertaApplication)
            modules(KoinModule.modules())
        }
    }
}