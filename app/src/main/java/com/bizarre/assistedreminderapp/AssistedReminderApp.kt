package com.bizarre.assistedreminderapp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AssistedReminderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
    @Inject
    lateinit var workerFactory: HiltWorkerFactory



}
