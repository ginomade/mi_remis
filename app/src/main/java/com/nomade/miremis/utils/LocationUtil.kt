package com.nomade.miremis.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest

fun isLocationHighPrecision(context: Context, onResult: (Boolean) -> Unit) {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    val networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    if (gpsEnabled || networkEnabled) {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(context)

        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { response ->
            // Ubicación en modo de alta precisión
            onResult(true)
        }

        task.addOnFailureListener {
            // Modo de ubicación no es de alta precisión
            onResult(false)
        }
    } else {
        // Mostrar un AlertDialog pidiendo al usuario que active la ubicación
        AlertDialog.Builder(context)
            .setTitle("Activar Localización")
            .setMessage("La ubicación con alta precisión es necesaria. ¿Deseas activarla ahora?")
            .setPositiveButton("Sí") { _, _ ->
                // Abrir configuración de ubicación
                context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("Cancelar") { _, _ ->
                // El usuario decide no activar la ubicación
                onResult(false)
            }
            .setCancelable(false)
            .show()
    }
}

