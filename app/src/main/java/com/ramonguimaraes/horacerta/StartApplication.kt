package com.ramonguimaraes.horacerta

import android.app.Application
import com.ramonguimaraes.horacerta.di.KoinManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class StartApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@StartApplication)
            modules(KoinManager.getModules())
        }
    }
}