package com.bizarre.assistedreminderapp.ui.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bizarre.assistedreminderapp.Graph
import com.bizarre.assistedreminderapp.R
import com.bizarre.assistedreminderapp.location.LocationManager
import com.bizarre.assistedreminderapp.location.LocationRepository
import com.bizarre.assistedreminderapp.ui.home.AppViewModel
import com.bizarre.core_domain.entity.Reminder

import com.bizarre.core_domain.repository.ReminderRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
        Log.d("","NOTIFYYYYYYYYYYYYYYYYYYYYYYYYYYY 9: " + rep.toString())
       // val reminders = repo.loadAllReminders()

        val reminder = Reminder(
            message = "",
            location_x = 0.0,
            location_y = 0.0,
            userId = 0,
            reminder_date = LocalDateTime.now(),
            creation_date = LocalDateTime.now(),
            is_seen = false

        )
       return try {

            LocationRepository.setCurrentReminder(rems[0])



           val formatter = DateTimeFormatter.ofPattern("dd MM yyyy")
           val date1 = reminder.reminder_date.toLocalDate().format(formatter)
           val notificationId = 2;
           val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_id")
               .setSmallIcon(R.drawable.ic_launcher_foreground)
               .setContentTitle("New reminder!!!")
               .setContentText(reminder.message)
               .setPriority(NotificationCompat.PRIORITY_HIGH)
           with(NotificationManagerCompat.from(Graph.appContext)) {
               if (ActivityCompat.checkSelfPermission(
                       Graph.appContext,
                       Manifest.permission.POST_NOTIFICATIONS
                   ) != PackageManager.PERMISSION_GRANTED
               ) {
                   Log.d("","NOTIFYYYYYYYYYYYYYYYYYYYYYYYYYYY 222222222")
                   // TODO: Consider calling
                   //    ActivityCompat#requestPermissions
                   // here to request the missing permissions, and then overriding
                   //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                   //                                          int[] grantResults)
                   // to handle the case where the user grants the permission. See the documentation
                   // for ActivityCompat#requestPermissions for more details.

               }
               Log.d("","NOTIFYYYYYYYYYYYYYYYYYYYYYYYYYYY")
               notify(notificationId, builder.build())
           }
         //  NotificationManager().createReminderNotification(rems[0])
           Log.d("","NOTIFYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY 7777777777")

           val outPutData = Data.Builder()
               .putString(KEY_WORKER,"" + rep.toString())
               .build()
            Result.success(outPutData)

        } catch (e: Exception) {
            Result.failure()
        }
    }
    companion object{
        const val KEY_WORKER = "key_worker"
    }

}