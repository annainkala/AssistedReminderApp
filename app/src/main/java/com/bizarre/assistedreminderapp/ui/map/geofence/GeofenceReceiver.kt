package com.bizarre.assistedreminderapp.ui.map.geofence

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bizarre.assistedreminderapp.Graph
import com.bizarre.assistedreminderapp.MainActivity
import com.bizarre.assistedreminderapp.R
import com.bizarre.assistedreminderapp.location.LocationRepository
import com.bizarre.assistedreminderapp.ui.home.AppViewModel
import com.bizarre.assistedreminderapp.ui.reminder.ReminderState
import com.bizarre.assistedreminderapp.ui.utils.createLocationNotification
import com.bizarre.core_domain.entity.Reminder
import com.bizarre.core_domain.repository.ReminderRepository
import com.bizarre.core_domain.repository.UserRepository
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import java.time.format.DateTimeFormatter
import javax.inject.Inject

const val GEOFENCE_RADIUS = 200
const val GEOFENCE_ID = "REMINDER_GEOFENCE_ID"
const val GEOFENCE_EXPIRATION = 10 * 24 * 60 * 60 * 1000 // 10 days
const val GEOFENCE_DWELL_DELAY =  10 * 1000 // 10 secs // 2 minutes
const val GEOFENCE_LOCATION_REQUEST_CODE = 12345
const val CAMERA_ZOOM_LEVEL = 13f
const val LOCATION_REQUEST_CODE = 123

class GeofenceReceiver  : BroadcastReceiver() {

    var id: Long = 0
    lateinit var text: String

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)
            val geofencingTransition = geofencingEvent.geofenceTransition

            if (geofencingTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofencingTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
                // Retrieve data from intent
                if (intent != null) {
                   id = intent.getLongExtra("id",0)!!
                    text = intent.getStringExtra("message")!!
                }



                if (LocationRepository.reminder == LocationRepository.reminders[id.toInt()]){
                    createLocationNotification(LocationRepository.reminders[id.toInt()],false)
                }
                else{
                    createLocationNotification(LocationRepository.reminders[id.toInt()],true)
                }


                // remove geofence
                val triggeringGeofences = geofencingEvent.triggeringGeofences
               MainActivity.removeGeofences(context, triggeringGeofences)
            }
        }
    }
}

