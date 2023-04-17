package com.bizarre.assistedreminderapp.ui.map

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
import androidx.navigation.NavController
import com.bizarre.assistedreminderapp.location.LocationRepository
import com.bizarre.assistedreminderapp.ui.utils.rememberMapViewWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch
import java.util.*


@Composable
fun MapScreen(navController: NavController) {

    val mapView = rememberMapViewWithLifecycle()
    val  coroutineScope = rememberCoroutineScope()

    val latlng = LocationRepository.getLocation2()


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
                val location = latlng
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(location,15f)
                )
                val markerOptions = MarkerOptions()
                    .title("Reminder")
                    .position(location)
                map.addMarker(markerOptions)
                setMapLongCLick(map = map, navController = navController)

            }
        }
    }




}

private fun setMapLongCLick(
    navController: NavController,
    map: GoogleMap
){
    map.setOnMapLongClickListener { latlng->
        val snippet = String.format(
            Locale.getDefault(),
            "Lat: %1$.2f, Lng %2$.2f",
            latlng.latitude,
            latlng.longitude

        )

        LocationRepository.setLocation2(latlng)
        map.addMarker(
            MarkerOptions().position(latlng).title("Reminder location").snippet(snippet)
        ).apply {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("location_data",latlng)
            navController.popBackStack()
        }
    }
}
