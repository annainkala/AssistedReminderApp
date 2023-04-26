package com.bizarre.assistedreminderapp.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import java.util.concurrent.TimeUnit


class LocationManager(){
    private var currentLocation: Location? = null
    fun getLocation(context: Context):LatLng{
        val loc = mutableStateOf(LatLng(0.00,0.00))

        val fusedLocationManager = LocationServices.getFusedLocationProviderClient(context)


        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) { // alse geen permissie hebben just return, anders voer functie location uit
            Log.d("","LATTTTTTT:::: " + loc.value.toString())

        }

        val locationRequest = LocationRequest.create().apply {

            interval = TimeUnit.SECONDS.toMillis(60)

            fastestInterval = TimeUnit.SECONDS.toMillis(30)

            maxWaitTime = TimeUnit.MINUTES.toMillis(2)

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

       val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                currentLocation = locationResult.lastLocation

               // LocationRepository.setLocation2(LatLng(currentLocation?.latitude!!,currentLocation?.longitude!!))


            }
        }
        fusedLocationManager.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        fusedLocationManager.lastLocation.addOnSuccessListener { location ->
            location.also {
                loc.value = LatLng(
                    it.latitude, it.longitude

                )

                Log.d("","LAT:::: " + loc.value.toString())
              //  LocationRepository.setLocation2(loc.value)


            }
        }

        return loc.value
    }
}
