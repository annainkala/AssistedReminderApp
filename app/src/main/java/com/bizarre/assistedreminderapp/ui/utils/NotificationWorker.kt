package com.bizarre.assistedreminderapp.ui.utils

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bizarre.assistedreminderapp.location.LocationClient
import com.bizarre.assistedreminderapp.location.LocationRepository
import com.bizarre.assistedreminderapp.ui.home.AppViewModel

import com.bizarre.core_domain.repository.ReminderRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDateTime
import javax.inject.Inject

@HiltWorker
class NotificationWorker2 @AssistedInject constructor(
   @Assisted appContext: Context,
   @Assisted workerParams: WorkerParameters,





):  CoroutineWorker(appContext, workerParams) {
   // val context = appContext
    //val work =  workerDependency
    val appContext2 = appContext


    override suspend fun doWork(): Result {

     //  work.reminders.toString()
        val rep = LocationRepository.getLocation2()
        val rems = LocationRepository.getReminderList()

       // val reminders = repo.loadAllReminders()

       return try {


           Log.d("","LOCATION:::::::: " + rep.toString())
           Log.d("","REMS:::::::: " + rems.size.toString())
            Result.success()

        } catch (e: Exception) {
            Result.failure()
        }
    }

}