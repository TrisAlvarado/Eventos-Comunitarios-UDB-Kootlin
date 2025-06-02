package com.udb.eventoscomunitarios

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EventosUDBApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}