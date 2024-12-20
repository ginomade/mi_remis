import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bugfender.sdk.Bugfender
import com.google.android.gms.maps.model.LatLng
import com.nomade.miremis.GeocodingViewModel
import com.nomade.miremis.HelperViewModel
import com.nomade.miremis.MainViewModel
import com.nomade.miremis.R
import com.nomade.miremis.utils.Constants.BUGFENDER_KEY
import com.nomade.miremis.utils.Constants.DISTANCIA_DESTINO_KEY
import com.nomade.miremis.utils.Constants.DISTANCIA_MOVIL_KEY
import com.nomade.miremis.utils.Constants.USER_EMAIL_KEY
import com.nomade.miremis.utils.Constants.USER_GEOPOS_KEY
import com.nomade.miremis.utils.SharedPrefsUtil
import com.nomade.miremis.utils.validLocation
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


@Composable
fun MyMapScreen(
    geoMoviles: List<String>,
    assignedMovilLocation: LatLng?,
    viewModel: HelperViewModel
) {

    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    val addressViewModel: GeocodingViewModel = viewModel()
    val mViewModel: MainViewModel = viewModel()
    val destinoLocation by viewModel.destinoLocation.collectAsState()
    val origenLocation by viewModel.origenLocation.collectAsState()
    var locationOverlay: MyLocationNewOverlay? by remember { mutableStateOf(null) }
    val context = LocalContext.current
    var coordenadasActuales by remember { mutableStateOf<GeoPoint?>(null) }

    var puntoInicio = GeoPoint(0, 0)
    var puntoDestino = GeoPoint(0, 0)
    // Estado mutable para almacenar la ruta
    var ruta by remember { mutableStateOf<List<GeoPoint>>(emptyList()) }
    var rutaMovilAsignado by remember { mutableStateOf<List<GeoPoint>>(emptyList()) }
    rutaMovilAsignado = emptyList()

    if (origenLocation != null && destinoLocation != null) {
        puntoInicio = GeoPoint(origenLocation!!.latitude, origenLocation!!.longitude)
        puntoDestino = GeoPoint(destinoLocation!!.latitude, destinoLocation!!.longitude)
        rutaMovilAsignado = emptyList()
    }
    var puntoInicioViaje = GeoPoint(0, 0)
    var puntoDestinoViaje = GeoPoint(0, 0)
    if (userLocation != null && assignedMovilLocation != null) {
        puntoInicioViaje = GeoPoint(assignedMovilLocation.latitude, assignedMovilLocation.longitude)
        puntoDestinoViaje = GeoPoint(userLocation!!.latitude, userLocation!!.longitude)
        ruta = emptyList()
    }

    // Observar la señal de reinicio del mapa
    val resetMap by viewModel.resetMap.collectAsState(false)


    fun obtenerRuta() {
        addressViewModel.obtenerRuta(
            context,
            origenLocation!!,
            destinoLocation!!
        ) { geocodingResult ->
            if (geocodingResult != null) {
                ruta = geocodingResult
            }
        }
    }

    fun obtenerRutaAMovil() {
        if (userLocation != null) {
            addressViewModel.obtenerRuta(
                context,
                assignedMovilLocation!!,
                userLocation!!
            ) { geocodingResult ->
                if (geocodingResult != null) {
                    rutaMovilAsignado = geocodingResult
                }
            }
        }
    }

    LaunchedEffect(resetMap) {
        if (resetMap) {
            // Reiniciar el mapa
            userLocation?.let {
                // Limpiar marcadores
                viewModel.resetMapComplete() // Indicar que el reset se ha completado

                ruta = emptyList()
                //rutaMovilAsignado = emptyList()
            }
        }
    }

    /*LaunchedEffect(Unit) {
        try {
            val locationResult = fusedLocationClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val location = task.result
                    if (location != null) {
                        userLocation = LatLng(location.latitude, location.longitude)
                        val geopos = "${location.latitude},${location.longitude}"
                        SharedPrefsUtil.set(USER_GEOPOS_KEY, geopos)

                        BuscarMovilesCercanos(mainViewModel)

                        //busco direccion origen aproximada
                        addressViewModel.geocodeAddress(
                            context,
                            userLocation!!.latitude.toString(),
                            userLocation!!.longitude.toString()
                        ) { geocodingResult ->
                            if (geocodingResult != null) {
                                SharedPrefsUtil.set(USER_ORIGEN_DETECTED_KEY, geocodingResult)
                                viewModel.setAddress(geocodingResult)
                            }
                        }
                    }
                }
            }
        } catch (e: SecurityException) {
            // Handle exception
            Toast.makeText(context, "Error en localización.", Toast.LENGTH_SHORT).show()
        }
    }*/

    /*if(validLocation(destinoLocation)) {
        obtenerRuta()
    }
    if(validLocation(assignedMovilLocation)) {
        obtenerRutaAMovil()
    }*/

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                val mapView = MapView(context)

                mapView.setMultiTouchControls(true)

                mapView.controller.setZoom(15.0)

                // Añadir brújula
                val compassOverlay = CompassOverlay(context, mapView)
                compassOverlay.enableCompass()
                mapView.overlays.add(compassOverlay)

                val overlay = MyLocationNewOverlay(mapView).apply {
                    enableMyLocation()
                    enableFollowLocation() // Seguir la ubicación del usuario automáticamente

                }
                locationOverlay = overlay
                locationOverlay!!.runOnFirstFix {
                    val userLocation = locationOverlay!!.myLocation
                    if (userLocation != null) {
                        Handler(Looper.getMainLooper()).post {
                            //mapView.controller.animateTo(userLocation)
                            coordenadasActuales =
                                userLocation // Actualizar estado con las coordenadas
                            BuscarMovilesCercanos(mViewModel)
                        }
                    }
                }

                mapView.overlays.add(overlay)

                val handler = Handler(Looper.getMainLooper())
                val runnable = object : Runnable {
                    override fun run() {
                        // Obtener la ubicación actual y actualizar el estado
                        val userLocation = locationOverlay!!.myLocation
                        if (userLocation != null) {
                            coordenadasActuales = userLocation
                        }
                        // Repetir la tarea cada 2 segundos
                        handler.postDelayed(this, 2000)
                    }
                }
                handler.post(runnable) // Iniciar el Handler

                mapView
            },
            update = { mapView ->

                coordenadasActuales?.let { location ->
                    val geopos = "${location.latitude},${location.longitude}"
                    SharedPrefsUtil.set(USER_GEOPOS_KEY, geopos)
                    userLocation = LatLng(location.latitude, location.longitude)
                }


                if (ruta.isNotEmpty()) {
                    mapView.overlays.clear()
                    logBf("ruta")
                    locationOverlay!!.disableFollowLocation()
                    val markerInicio = Marker(mapView)
                    markerInicio.position = puntoInicio
                    markerInicio.title = "Inicio"
                    markerInicio.icon = ContextCompat.getDrawable(context, R.drawable.ic_hombre_40)
                    mapView.overlays.add(markerInicio)

                    val markerDestino = Marker(mapView)
                    markerDestino.position = puntoDestino
                    markerDestino.title = "Destino"
                    mapView.overlays.add(markerDestino)
                    val polyline = Polyline()
                    val paint = polyline.outlinePaint.apply {
                        color =
                            Color.GREEN          // Cambiar a cualquier color, por ejemplo, Color.RED o Color.parseColor("#FF0000")
                        strokeWidth = 10f           // Grosor de la línea
                        style = Paint.Style.STROKE  // Solo contorno
                        isAntiAlias = true          // Suavizar bordes
                    }
                    ruta.forEach { point ->
                        polyline.addPoint(point)
                    }
                    mapView.overlays.add(polyline)
                    // Crear el BoundingBox que incluye todos los puntos de la ruta
                    val boundingBox = BoundingBox.fromGeoPointsSafe(ruta)
                    // Centrar el mapa y ajustar el zoom para mostrar toda la ruta
                    mapView.zoomToBoundingBox(boundingBox, true, 70)

                    guardarDistancia(ruta)
                    viewModel.setUpdatePrecio(true)
                    mapView.invalidate() // Refrescar el mapa para mostrar la ruta

                } else if (rutaMovilAsignado.isNotEmpty()) {
                    mapView.overlays.clear()
                    logBf("rutaMovilAsignado")
                    locationOverlay!!.disableFollowLocation()

                    val polyline = Polyline()
                    val paint = polyline.outlinePaint.apply {
                        color =
                            Color.BLUE          // Cambiar a cualquier color, por ejemplo, Color.RED o Color.parseColor("#FF0000")
                        strokeWidth = 10f           // Grosor de la línea
                        style = Paint.Style.STROKE  // Solo contorno
                        isAntiAlias = true          // Suavizar bordes
                    }
                    rutaMovilAsignado.forEach { point ->
                        polyline.addPoint(point)
                    }
                    mapView.overlays.add(polyline)

                    val markerMovilInicio = Marker(mapView)
                    markerMovilInicio.position = puntoInicioViaje
                    markerMovilInicio.title = "Inicio"
                    markerMovilInicio.icon =
                        ContextCompat.getDrawable(context, R.drawable.car_green)
                    mapView.overlays.add(markerMovilInicio)

                    val markerMovilDestino = Marker(mapView)
                    markerMovilDestino.position = puntoDestinoViaje
                    markerMovilDestino.title = "Destino"
                    markerMovilDestino.icon =
                        ContextCompat.getDrawable(context, R.drawable.ic_hombre_40)
                    mapView.overlays.add(markerMovilDestino)

                    // Crear el BoundingBox que incluye todos los puntos de la ruta
                    val boundingBox = BoundingBox.fromGeoPointsSafe(rutaMovilAsignado)
                    // Centrar el mapa y ajustar el zoom para mostrar toda la ruta
                    mapView.zoomToBoundingBox(boundingBox, true, 70)

                    guardarDistanciaMovil(ruta)
                    mapView.invalidate() // Refrescar el mapa para mostrar la ruta
                } else if (validLocation(assignedMovilLocation)) {
                    logBf("obtenerRutaAMovil")
                    obtenerRutaAMovil()
                } else if (validLocation(destinoLocation)) {
                    logBf("obtenerRuta")
                    obtenerRuta()
                } else {
                    mapView.overlays.clear()
                    logBf("else")
                    locationOverlay!!.enableFollowLocation()
                    // Añadir el overlay de ubicación cuando los permisos estén concedidos
                    locationOverlay?.let { overlay ->
                        overlay.enableMyLocation() // Habilitar la ubicación
                        overlay.enableFollowLocation() // Seguir la ubicación del usuario

                        // Centrar el mapa en la ubicación actual
                        overlay.runOnFirstFix {
                            val userLocation = overlay.myLocation
                            if (userLocation != null) {
                                Handler(Looper.getMainLooper()).post {
                                    mapView.controller.animateTo(userLocation)
                                }
                            }
                        }
                    }

                    if (geoMoviles.isNotEmpty()) {
                        geoMoviles.forEach {
                            val marker = Marker(mapView)
                            val markerPosition = GeoPoint(
                                it.split(",")[0].toDouble(),
                                it.split(",")[1].toDouble()
                            )  // Coordenadas de Londres
                            marker.position = markerPosition
                            marker.setAnchor(
                                Marker.ANCHOR_CENTER,
                                Marker.ANCHOR_BOTTOM
                            ) // Anclar el marcador en la parte inferior
                            marker.title =
                                "Movil"  // Título que aparece al hacer clic en el marcador
                            marker.icon = ContextCompat.getDrawable(context, R.drawable.car)
                            mapView.overlays.add(marker) // Añadir el marcador al mapa
                        }
                    }

                    mapView.overlays.add(locationOverlay)
                    mapView.invalidate() // Refrescar el mapa para mostrar los cambios
                }
            }
        )
    }

}

private fun guardarDistanciaMovil(ruta: List<GeoPoint>) {
    val distanciaTotalMetros = calcularDistanciaRuta(ruta)
    SharedPrefsUtil.set(DISTANCIA_MOVIL_KEY, distanciaTotalMetros)
}

private fun guardarDistancia(ruta: List<GeoPoint>) {
    val distanciaTotalMetros = calcularDistanciaRuta(ruta)
    SharedPrefsUtil.set(DISTANCIA_DESTINO_KEY, distanciaTotalMetros)
}

private fun BuscarMovilesCercanos(mainViewModel: MainViewModel) {
    //mando pedido de moviles cerca
    val savedEmail = SharedPrefsUtil.get(USER_EMAIL_KEY, "")
    val userGeo = SharedPrefsUtil.get(USER_GEOPOS_KEY, "")
    mainViewModel.buscarMoviles(savedEmail, userGeo)
}

fun calcularDistanciaRuta(ruta: List<GeoPoint>): Int {
    var distanciaTotal = 0.0

    // Iterar sobre los puntos y sumar las distancias entre puntos consecutivos
    for (i in 0 until ruta.size - 1) {
        val puntoActual = ruta[i]
        val siguientePunto = ruta[i + 1]
        distanciaTotal += puntoActual.distanceToAsDouble(siguientePunto)
    }

    return distanciaTotal.toInt() // Distancia en metros
}

private fun logBf(text: String) {
    if (SharedPrefsUtil.get(BUGFENDER_KEY, false))
        Bugfender.d("MAPSCREEN", text)

    Log.w("MAPSCREEN", text)
}