package com.bizarre.assistedreminderapp.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bizarre.assistedreminderapp.*
import com.bizarre.assistedreminderapp.location.LocationManager
import com.bizarre.assistedreminderapp.ui.home.AppViewModel
import com.bizarre.assistedreminderapp.ui.map.geofence.GeofenceReceiver
import com.bizarre.assistedreminderapp.ui.reminder.ReminderState
import com.bizarre.assistedreminderapp.ui.utils.checkForGeoFenceEntry

import com.bizarre.assistedreminderapp.ui.utils.rememberMapViewWithLifecycle
import com.bizarre.core_domain.entity.Reminder
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit


@Composable
fun MapScreen(navController: NavController,id:String,latlng:String, viewModel:AppViewModel = hiltViewModel()) {


    val reminderViewState by viewModel.reminderState.collectAsState()
    when (reminderViewState) {
        is ReminderState.Loading -> {}
        is  ReminderState.Success -> {


           // val reminder = remember{ mutableStateOf((reminderViewState as ReminderState.Success).selectedReminder) }
            val reminders = remember{ mutableStateOf((reminderViewState as ReminderState.Success).data) }



            val mapView = rememberMapViewWithLifecycle()
            val coroutineScope = rememberCoroutineScope()
            val getGeoFencingClient = LocationServices.getGeofencingClient(Graph.appContext)
            // val latlng = LocationRepository.getLocation2()


            Log.d("AAAAAAAA______", "LATLONG: " + latlng.toString())

            val lat = latlng.split(",")[0].toDouble()
            val lng = latlng.split(",")[1].toDouble()
            val id1 = id.toLong()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(bottom = 40.dp)
            )
            {
                AndroidView({ mapView }) { mapView ->
                    coroutineScope.launch {
                        val map = mapView.awaitMap()
                        map.uiSettings.isZoomControlsEnabled = true



                        val location = LatLng(lat, lng)
                        map.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(location, 15f)
                        )
                        val markerOptions = MarkerOptions()
                            .title("Reminder")
                            .position(location)
                        map.addMarker(markerOptions)
                        setMapLongCLick(
                            map = map,
                            navController = navController,
                            geoFenceClient = getGeoFencingClient,
                            viewModel = viewModel,
                           reminder = reminders.value[id1.toInt()])

                        setMapLongCLick(
                            map = map,
                            navController = navController,
                            geoFenceClient = getGeoFencingClient,
                            viewModel = viewModel,
                            reminder = reminders.value[id1.toInt()])





                }

                }
            }


        }
        else -> {}
    }

}

private fun setMapLongCLick(
    navController: NavController,
    map: GoogleMap,
    geoFenceClient: GeofencingClient,
    viewModel:AppViewModel,
    reminder:Reminder
){

    map.setOnMapLongClickListener { latlng->
        val snippet = String.format(
            Locale.getDefault(),
            "Lat: %1$.2f, Lng %2$.2f",
            latlng.latitude,
            latlng.longitude

        )



        reminder.location_x = latlng.latitude
        reminder.location_y = latlng.longitude
        viewModel.saveReminder(reminder)



        map.addMarker(
            MarkerOptions().position(latlng).title("Reminder location").snippet(snippet)
        ).apply {
            navController.previousBackStackEntry?.savedStateHandle?.set("latlng",latlng.latitude.toString() + "," + latlng.longitude.toString())
            navController.popBackStack()
        }
    }
}



private fun setMapOnCLick(
    navController: NavController,
    map: GoogleMap,
    geoFenceClient: GeofencingClient,
    id:Long,
    viewModel:AppViewModel,
    reminders:List<Reminder>
){










        map.setOnMapClickListener{ latlng->
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.2f, Lng %2$.2f",
                latlng.latitude,
                latlng.longitude

            )
          for (reminder in reminders){
              if (checkForGeoFenceEntry(latlng,reminder)){

                  map.addMarker(
                      MarkerOptions().position(latlng).title("Reminder location").snippet(snippet)
                  ).apply {

                  }
              }
          }




    }


}

