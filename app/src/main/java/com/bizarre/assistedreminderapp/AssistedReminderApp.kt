package com.bizarre.assistedreminderapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AssistedReminderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}