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
import com.bizarre.assistedreminderapp.ui.utils.rememberMapViewWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch


@Composable
fun MapScreen(navController: NavController) {

   val mapView = rememberMapViewWithLifecycle()
  val  coroutineScope = rememberCoroutineScope()


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
                val location = LatLng(65.000, 25.00)
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(location,15f)
                )
            }
        }
        }




}