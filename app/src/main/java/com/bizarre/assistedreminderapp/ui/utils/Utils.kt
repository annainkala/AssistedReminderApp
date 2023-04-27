package com.bizarre.assistedreminderapp.ui.utils

import android.Manifest
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
import com.bizarre.assistedreminderapp.R
import com.bizarre.core_domain.entity.Reminder
import java.text.SimpleDateFormat
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

fun createReminderNotification(reminder: Reminder){
    Log.d("    ", " REMINDER 000000")
    val formatter = DateTimeFormatter.ofPattern("dd MM yyyy")
    val date1 = reminder.reminder_date.toLocalDate().format(formatter)
    val notificationId = 2;
    val builder = NotificationCompat.Builder(Graph.appContext,"channel_id")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(reminder.message)
        .setContentText("REMINDR!!!!")
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


