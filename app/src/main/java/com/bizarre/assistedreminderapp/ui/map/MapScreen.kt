package com.bizarre.assistedreminderapp.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
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
fun MapScreen(
    navController: NavController,
    id: String,
    latlng: String,
    viewModel: AppViewModel = hiltViewModel()
) {

    Log.d("IIIII ", "INDEX 2:::: " + id)

    val reminderViewState by viewModel.reminderState.collectAsState()
    when (reminderViewState) {
        is ReminderState.Loading -> {}
        is ReminderState.Success -> {


            // val reminder = remember{ mutableStateOf((reminderViewState as ReminderState.Success).selectedReminder) }
            val reminders =
                remember { mutableStateOf((reminderViewState as ReminderState.Success).data) }


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
                            reminder = getReminder(id.toInt(), reminders.value)
                        )

                        setMapOnCLick(
                            map = map,
                            navController = navController,
                            geoFenceClient = getGeoFencingClient,
                            id = id.toLong(),
                            viewModel = viewModel,
                            reminders = reminders.value,
                        )


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
    viewModel: AppViewModel,
    reminder: Reminder
) {

    map.setOnMapLongClickListener { latlng ->
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
            navController.previousBackStackEntry?.savedStateHandle?.set(
                "latlng",
                latlng.latitude.toString() + "," + latlng.longitude.toString()
            )
            navController.popBackStack()
        }
    }
}


private fun setMapOnCLick(
    navController: NavController,
    map: GoogleMap,
    geoFenceClient: GeofencingClient,
    id: Long,
    viewModel: AppViewModel,
    reminders: List<Reminder>
) {



    map.setOnMapClickListener {

        val reminder0 = getReminder(id.toInt(),reminders);
        val latlng1 = LatLng(reminder0.location_x,reminder0.location_y)

           // latlng1 ->
        val snippet = String.format(
            Locale.getDefault(),
            "Lat: %1$.2f, Lng %2$.2f",
            latlng1.latitude,
            latlng1.longitude

        )
        Log.d("MAP0","CLICK 0: ")
        for (reminder in reminders) {
            Log.d("MAP0","CLICK 1: ")
            if (checkForGeoFenceEntry(latlng1, reminder)) {

                map.addMarker(
                    MarkerOptions().position(LatLng(reminder.location_x,reminder.location_y)).title("Reminder location").snippet(snippet)
                ).apply {

                }
            }
        }


    }


}

/**
 * Manages all location related tasks for the app.
 */


lateinit var locationCallback: LocationCallback

lateinit var locationProvider: FusedLocationProviderClient

@SuppressLint("MissingPermission")
@Composable
fun getUserLocation(context: Context): LatLng {

    // The Fused Location Provider provides access to location APIs.
    locationProvider = LocationServices.getFusedLocationProviderClient(context)

    var currentUserLocation by remember { mutableStateOf(LatLng(0.0,0.0)) }

    DisposableEffect(key1 = locationProvider) {
        locationCallback = object : LocationCallback() {
            //1
            override fun onLocationResult(result: LocationResult) {
                /**
                 * Option 1
                 * This option returns the locations computed, ordered from oldest to newest.
                 * */
                for (location in result.locations) {
                    // Update data class with location data
                    currentUserLocation = LatLng(location.latitude, location.longitude)
                    Log.d("LOCATION_TAG", "${location.latitude},${location.longitude}")
                }


                /**
                 * Option 2
                 * This option returns the most recent historical location currently available.
                 * Will return null if no historical location is available
                 * */
                locationProvider.lastLocation
                    .addOnSuccessListener { location ->
                        location?.let {
                            val lat = location.latitude
                            val long = location.longitude
                            // Update data class with location data
                            currentUserLocation = LatLng(lat, long)

                        }
                    }
                    .addOnFailureListener {
                        Log.e("Location_error", "${it.message}")
                    }

            }
        }
        //2

            locationUpdate()

        //3
        onDispose {
            stopLocationUpdate()
        }
    }
    //4
    return currentUserLocation
}

//data class to store the user Latitude and longitude
@SuppressLint("MissingPermission")
fun locationUpdate() {
    locationCallback.let {
        //An encapsulation of various parameters for requesting
        // location through FusedLocationProviderClient.
        val locationRequest: LocationRequest =
            LocationRequest.create().apply {
                interval = TimeUnit.SECONDS.toMillis(60)
                fastestInterval = TimeUnit.SECONDS.toMillis(30)
                maxWaitTime = TimeUnit.MINUTES.toMillis(2)
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
        //use FusedLocationProviderClient to request location update
        locationProvider.requestLocationUpdates(
            locationRequest,
            it,
            Looper.getMainLooper()
        )
    }

}
fun stopLocationUpdate() {
    try {
        //Removes all location updates for the given callback.
        val removeTask = locationProvider.removeLocationUpdates(locationCallback)
        removeTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("LOCATION_TAG", "Location Callback removed.")
            } else {
                Log.d("LOCATION_TAG", "Failed to remove Location Callback.")
            }
        }
    } catch (se: SecurityException) {
        Log.e("LOCATION_TAG", "Failed to remove Location Callback.. $se")
    }
}
private fun getReminder(id:Int, reminders:List<Reminder>):Reminder{

    for (reminder in reminders){

        if (reminder.reminderId.toInt() == id){
            return reminder
        }
    }
    return reminders[0]

}



