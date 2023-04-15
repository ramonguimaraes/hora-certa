package com.ramonguimaraes.horacerta

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HoraCertaApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@HoraCertaApplication)
            KoinModule.modules()
        }
    }
}