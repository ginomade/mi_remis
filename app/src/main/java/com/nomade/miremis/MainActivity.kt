package com.nomade.miremis

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.bugfender.sdk.Bugfender
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.nomade.miremis.screens.ContactDetails
import com.nomade.miremis.screens.MainScreen
import com.nomade.miremis.screens.MensajesScreen
import com.nomade.miremis.screens.PrivacyScreen
import com.nomade.miremis.screens.ReclamosScreen
import com.nomade.miremis.screens.UserInfoScreen
import com.nomade.miremis.ui.theme.MiRemisTheme
import com.nomade.miremis.utils.Constants.APP_STATE_KEY
import com.nomade.miremis.utils.Constants.BUGFENDER_KEY
import com.nomade.miremis.utils.Constants.MOVIL_KEY
import com.nomade.miremis.utils.Constants.OBSERVACIONES_KEY
import com.nomade.miremis.utils.Constants.PRIVACY_ADD
import com.nomade.miremis.utils.Constants.RESERVA_KEY
import com.nomade.miremis.utils.Constants.SMALL_SCREEN_KEY
import com.nomade.miremis.utils.Constants.USER_DESTINO_KEY
import com.nomade.miremis.utils.Constants.USER_EMAIL_KEY
import com.nomade.miremis.utils.Constants.USER_GEOPOS_KEY
import com.nomade.miremis.utils.Constants.USER_NAME_KEY
import com.nomade.miremis.utils.Constants.USER_ORIGEN_DETECTED_KEY
import com.nomade.miremis.utils.Constants.USER_ORIGEN_KEY
import com.nomade.miremis.utils.Constants.USER_PHONE_KEY
import com.nomade.miremis.utils.Constants.WAITING_STATE_KEY
import com.nomade.miremis.utils.SharedPrefsUtil
import com.nomade.miremis.utils.isLocationHighPrecision
import org.osmdroid.config.Configuration
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private val TAG_MAIN = "MainActivity"
    private lateinit var viewModel: MainViewModel
    private lateinit var helperViewModel: HelperViewModel
    private lateinit var addViewModel: GeocodingViewModel

    private var geopos = "" // lat,lon
    private val requestingLocationUpdates = true

    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    var estadoGlobal = ""

    var activeTimer = false

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue with the action or workflow in your app.
                showNotificationSettingsDialog()
            } else {
                // Explain to the user that the feature is unavailable because the features requires a permission that the user has denied.
            }
        }


    fun showNotificationSettingsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Habilitar Notificaciones")
            .setMessage("Para recibir notificaciones, por favor habilitalas en la configuración de la aplicación.")
            .setPositiveButton("Abrir Configuración") { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                startActivity(intent)

            }
            .setNegativeButton(
                "Cancelar",
                null
            )
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        window.addFlags(FLAG_KEEP_SCREEN_ON)
        SharedPrefsUtil.init(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        helperViewModel = ViewModelProvider(this).get(HelperViewModel::class.java)
        addViewModel = ViewModelProvider(this).get(GeocodingViewModel::class.java)
        enableEdgeToEdge()

        // Inicializar la configuración de osmdroid
        Configuration.getInstance()
            .load(applicationContext, getSharedPreferences("osm_pref", MODE_PRIVATE))

        //Send logs to bugfender
        SharedPrefsUtil.set(BUGFENDER_KEY, false)

        getScreenSize(context = this)

        helperViewModel.setOrigenGeopos(
            LatLng(
                0.0,
                0.0
            )
        )

        setContent {
            MiRemisTheme() {
                androidx.compose.material3.Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp {
                        MainAppContent()
                    }
                }

            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
            val bottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            view.updatePadding(bottom = bottom)
            insets
        }

        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the location.
            }

            else -> {
                // You can directly ask for the permission.
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(15)
            fastestInterval = TimeUnit.SECONDS.toMillis(5)
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }
        locationCallback = object : LocationCallback() {

            override fun onLocationResult(p0: LocationResult) {
                p0 ?: return
                for (location in p0.locations) {
                    val lat = location.latitude
                    val lon = location.longitude
                    Log.w(
                        TAG_MAIN,
                        "getCurrentLocation localizacion $lat,$lon"
                    )
                    geopos = "$lat,$lon"
                    helperViewModel.setNewGeopos(LatLng(lat, lon))

                    if (location != null) {

                        val geopos = "${location.latitude},${location.longitude}"
                        SharedPrefsUtil.set(USER_GEOPOS_KEY, geopos)

                        //BuscarMovilesCercanos(mainViewModel)
                        val savedEmail = SharedPrefsUtil.get(USER_EMAIL_KEY, "")
                        val userGeo = SharedPrefsUtil.get(USER_GEOPOS_KEY, "")
                        viewModel.buscarMoviles(savedEmail, userGeo)

                        //busco direccion origen aproximada
                        addViewModel.geocodeAddress(
                            applicationContext,
                            location.latitude.toString(),
                            location.longitude.toString()
                        ) { geocodingResult ->
                            if (geocodingResult != null) {
                                SharedPrefsUtil.set(USER_ORIGEN_DETECTED_KEY, geocodingResult)
                                helperViewModel.setAddress(geocodingResult)
                            }
                        }
                    }
                }
            }
        }

        isLocationHighPrecision(this) { isHighPrecision ->
            if (isHighPrecision) {
                // Ubicación en alta precisión activada
                println("Ubicación activada en alta precisión")
            } else {
                // El usuario canceló o no está activada
                println("Ubicación no activada en alta precisión")
            }
        }

        ValidarDatosCargados()
        viewModel.buscarTarifas()
    }

    fun getScreenSize(context: Context) {
        val displayMetrics = Resources.getSystem().displayMetrics
        val smallScreen = displayMetrics.heightPixels < 1900
        SharedPrefsUtil.set(SMALL_SCREEN_KEY, smallScreen)
    }

    fun ValidarDatosCargados() {
        val savedEmail = SharedPrefsUtil.get(USER_EMAIL_KEY, "")
        val celu = SharedPrefsUtil.get(USER_PHONE_KEY, "")
        val name = SharedPrefsUtil.get(USER_NAME_KEY, "")
        if (savedEmail == "" || celu == "" || name == "") {
            helperViewModel.setEstadoViaje(Estado.SIN_DATOS)
        }
    }

    override fun onResume() {
        super.onResume()
        activeTimer = true
        if (requestingLocationUpdates) startLocationUpdates()
        SharedPrefsUtil.set(WAITING_STATE_KEY, false)
        iniciarTimer()
        viewModel.buscarUltimoViaje(SharedPrefsUtil.get(USER_EMAIL_KEY, ""))
        viewModel.buscarHistorialViajes(SharedPrefsUtil.get(USER_EMAIL_KEY, ""))
        helperViewModel.setEstadoViaje(Estado.INICIO)
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
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
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
        activeTimer = false
        SharedPrefsUtil.set(USER_GEOPOS_KEY, "")
        SharedPrefsUtil.set(USER_DESTINO_KEY, "")
        SharedPrefsUtil.set(USER_ORIGEN_KEY, "")
        SharedPrefsUtil.set(OBSERVACIONES_KEY, "")
        SharedPrefsUtil.set(USER_ORIGEN_DETECTED_KEY, "")
        SharedPrefsUtil.set(RESERVA_KEY, "")

        startNotificationService()
    }

    fun startNotificationService() {
        val workRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setInitialDelay(20, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            "remisWork",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    private fun stopLocationUpdates() {
        if (::locationCallback.isInitialized) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        MyApp {
            MainAppContent()
        }
    }

    @Composable
    fun MyApp(content: @Composable () -> Unit) {
        MaterialTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White
            ) {
                content()
            }
        }
    }

    @Composable
    private fun MainAppContent(navController: NavHostController = rememberNavController()) {

        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "main") {
            composable("main") {
                MainScreen(
                    navController,
                    viewModel,
                    helperViewModel
                )
            }
            composable("userInfo") { UserInfoScreen(navController, helperViewModel) }
            composable("contacto") { ContactDetails(navController) }
            composable("privacidad") { PrivacyScreen(PRIVACY_ADD, navController) }
            composable("mensajes") { MensajesScreen(navController) }
            composable("reclamos") { ReclamosScreen(navController) }
        }

    }

    private fun iniciarTimer() {

        Timer().schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread { tareasPeriodicas() }
            }
        }, if (estadoGlobal == Estado.ASIGNADO.toString()) 10000 else 20000)
    }

    private fun tareasPeriodicas() {
        estadoGlobal = SharedPrefsUtil.get(APP_STATE_KEY, "")
        logBf(
            "ESTADO - {$estadoGlobal}"
        )
        if (estadoGlobal == Estado.ASIGNADO.toString()) {
            viewModel.buscarEstadoMovil(
                SharedPrefsUtil.get(USER_EMAIL_KEY, ""),
                SharedPrefsUtil.get(MOVIL_KEY, ""),
                SharedPrefsUtil.get(USER_GEOPOS_KEY, "")
            )
            Log.d(
                TAG_MAIN,
                "REQUEST ***********************************"
            )
            viewModel.hayMensajes(
                SharedPrefsUtil.get(USER_EMAIL_KEY, ""),
                SharedPrefsUtil.get(RESERVA_KEY, "")
            )
        }
        if (estadoGlobal != Estado.CON_ORIGEN.toString() && !SharedPrefsUtil.get(
                WAITING_STATE_KEY,
                true
            )
        ) {
            viewModel.buscarUltimoViaje(SharedPrefsUtil.get(USER_EMAIL_KEY, ""))
            Log.d(
                TAG_MAIN,
                "REQUEST RETRY"
            )
        }

        if (estadoGlobal == Estado.INICIO.toString()) {
            viewModel.hayMensajes(
                SharedPrefsUtil.get(USER_EMAIL_KEY, ""),
                SharedPrefsUtil.get(RESERVA_KEY, "")
            )
        }

        if (activeTimer) {
            iniciarTimer()
        }
    }

    private fun logBf(text: String) {
        if (SharedPrefsUtil.get(BUGFENDER_KEY, false))
            Bugfender.d(TAG_MAIN, text)

        Log.w(TAG_MAIN, text)
    }
}

