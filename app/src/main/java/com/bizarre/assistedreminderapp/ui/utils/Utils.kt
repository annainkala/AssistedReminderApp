package com.bizarre.assistedreminderapp.ui.utils

import android.Manifest
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import com.bizarre.assistedreminderapp.Graph
import com.bizarre.assistedreminderapp.MainActivity
import com.bizarre.assistedreminderapp.R
import com.bizarre.core_domain.entity.Reminder
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil.computeDistanceBetween

import java.time.format.DateTimeFormatter



@Composable
fun ReminderTopAppBar(navController:NavController){

    TopAppBar {
        IconButton(
            onClick = {
                navController.popBackStack()
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null
            )
        }
    }
}

fun createLocationNotification(reminder: Reminder, isDate:Boolean){




    Log.d("    ", " REMINDER 000000")
    val formatter = DateTimeFormatter.ofPattern("dd MM yyyy")
    val date1 = "";
    if(isDate){
        reminder.reminder_date.toLocalDate().format(formatter)
    }
    val activityIntent = Intent(Graph.appContext, MainActivity::class.java)
    val id = 0
    val pendingIntent = PendingIntent.getActivity(Graph.appContext, id, activityIntent, PendingIntent.FLAG_CANCEL_CURRENT)

    val notificationId = 2;
    val builder = NotificationCompat.Builder(Graph.appContext,"channel_id")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Reminder! " + date1)
        .setContentText(reminder.message)
        .setContentIntent(pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    with(NotificationManagerCompat.from(Graph.appContext)){
        if (ActivityCompat.checkSelfPermission(
                Graph.appContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        Log.d("    ", " REMINDER 111111")
        notify(notificationId,builder.build())
    }

}
val RADIUS= 3000



fun checkForGeoFenceEntry(currentLocation: LatLng, reminder:Reminder):Boolean{




val dist =computeDistanceBetween(currentLocation, LatLng(reminder.location_x,reminder.location_y))

        if(dist < RADIUS){
            return true
        }


    return false

}


