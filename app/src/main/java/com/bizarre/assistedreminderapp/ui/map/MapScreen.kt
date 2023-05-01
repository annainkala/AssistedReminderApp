package com.bizarre.assistedreminderapp.ui.map

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.bizarre.assistedreminderapp.*
import com.bizarre.assistedreminderapp.location.LocationRepository
import com.bizarre.assistedreminderapp.ui.map.geofence.GeofenceReceiver

import com.bizarre.assistedreminderapp.ui.utils.rememberMapViewWithLifecycle
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch
import java.util.*


@Composable
fun MapScreen(navController: NavController,id:String,latlng:String) {

    val mapView = rememberMapViewWithLifecycle()
    val  coroutineScope = rememberCoroutineScope()
    val getGeoFencingClient = LocationServices.getGeofencingClient(Graph.appContext)
   // val latlng = LocationRepository.getLocation2()

    Log.d("AAAAAAAA______","LATLONG: " + latlng.toString())

    val lat =     latlng.split(",")[0].toDouble()
   val lng = latlng.split(",")[1].toDouble()
    val id1 = id.toLong()
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
        .padding(bottom = 40.dp))
    {
        AndroidView({mapView}){
                mapView->
            coroutineScope.launch {
                val map = mapView.awaitMap()
                map.uiSettings.isZoomControlsEnabled = true
                val location = LatLng(lat,lng)
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(location,15f)
                )
                val markerOptions = MarkerOptions()
                    .title("Reminder")
                    .position(location)
                map.addMarker(markerOptions)
                setMapLongCLick(map = map, navController = navController, geoFenceClient = getGeoFencingClient,id=id1)

            }
        }
    }




}

private fun setMapLongCLick(
    navController: NavController,
    map: GoogleMap,
    geoFenceClient: GeofencingClient,
    id:Long
){
    map.setOnMapLongClickListener { latlng->
        val snippet = String.format(
            Locale.getDefault(),
            "Lat: %1$.2f, Lng %2$.2f",
            latlng.latitude,
            latlng.longitude

        )

        LocationRepository.reminders[id.toInt()].location_x = latlng.latitude
        LocationRepository.reminders[id.toInt()].location_y = latlng.longitude
        LocationRepository.update = true;
        createGeoFence(latlng,geoFenceClient,id)


        map.addMarker(
            MarkerOptions().position(latlng).title("Reminder location").snippet(snippet)
        ).apply {
            navController.previousBackStackEntry?.savedStateHandle?.set("latlng",latlng.latitude.toString() + "," + latlng.longitude.toString())
            navController.popBackStack()
        }
    }
}

private fun createGeoFence(location: LatLng,  geofencingClient: GeofencingClient, id:Long) {
    val geofence = Geofence.Builder()
        .setRequestId(GEOFENCE_ID)
        .setCircularRegion(location.latitude, location.longitude, GEOFENCE_RADIUS.toFloat())
        .setExpirationDuration(GEOFENCE_EXPIRATION.toLong())
        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_DWELL)
        .setLoiteringDelay(GEOFENCE_DWELL_DELAY)
        .build()

    val geofenceRequest = GeofencingRequest.Builder()
        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        .addGeofence(geofence)
        .build()

    val intent = Intent(Graph.appContext, GeofenceReceiver::class.java)
        .putExtra("id", id)
        .putExtra("message", "Geofence alert - ${location.latitude}, ${location.longitude}")

    val pendingIntent = PendingIntent.getBroadcast(
        Graph.appContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        if (ContextCompat.checkSelfPermission(
                Graph.appContext, Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
        }

       else {
            geofencingClient.addGeofences(geofenceRequest, pendingIntent)

        }

    }
    geofencingClient.addGeofences(geofenceRequest, pendingIntent)
}