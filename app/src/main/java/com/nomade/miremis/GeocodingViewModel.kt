package com.nomade.miremis

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.bugfender.sdk.Bugfender
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.nomade.miremis.net.GeoObject
import com.nomade.miremis.net.OSRMResponse
import com.nomade.miremis.net.OSRMService
import com.nomade.miremis.net.RetrofitService
import com.nomade.miremis.net.ReverseGeoObject
import com.nomade.miremis.utils.Constants.BUGFENDER_KEY
import com.nomade.miremis.utils.Constants.GEO_ROUTE_ADD
import com.nomade.miremis.utils.Constants.GET_RUTA_ACTIVE_KEY
import com.nomade.miremis.utils.SharedPrefsUtil
import com.nomade.miremis.utils.decodePolyline
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GeocodingViewModel : ViewModel() {

    var TAG_VIEWMODEL = "GeocodingViewModel"
    private var geocodeAddressRequestInProgress = false

    fun geocodeFromAddress(context: Context, address: String, onResult: (GeoObject?) -> Unit) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val finalAddress = "$address , Ushuaia, Tierra del Fuego"
                val call: Call<String?>? =
                    RetrofitService.RetrofitGeoInstance.api.geoCoding(
                        finalAddress,
                        1,
                        "json",
                        1
                    )
                call!!.enqueue(object : Callback<String?> {

                    override fun onResponse(
                        call: Call<String?>,
                        response: Response<String?>
                    ) {
                        if (response.isSuccessful()) {
                            logBf("geocodeFromAddress: ${response.body()}")

                            val data = obtenerGeoObject(response)
                            if (data.isNotEmpty()) {
                                onResult(data.get(0))
                            } else {
                                onResult(null)
                            }

                        } else {

                            onResult(null)
                        }
                    }

                    override fun onFailure(call: Call<String?>, t: Throwable) {
                        logBf(
                            "geocodeFromAddress: error - ${call.request()} , ${t.localizedMessage}"
                        )
                    }
                })


            } catch (e: Exception) {
                e.printStackTrace()
                onResult(null)
            }
        }
    }

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.w("GeocodingViewModel", "Exception handled: ${throwable.localizedMessage}")
    }

    fun geocodeAddress(context: Context, lat: String, lon: String, onResult: (String?) -> Unit) {
        if (geocodeAddressRequestInProgress) {
            return
        }
        geocodeAddressRequestInProgress = true
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {

                //https://nominatim.openstreetmap.org/reverse?format=json&lat=52.5487429714954&lon=-1.81602098644987&zoom=18&addressdetails=1
                val call: Call<String?>? =
                    RetrofitService.RetrofitGeoInstance.api.reverseGeoCoding(lat, lon, 18, 1)
                call!!.enqueue(object : Callback<String?> {

                    override fun onResponse(
                        call: Call<String?>,
                        response: Response<String?>
                    ) {
                        geocodeAddressRequestInProgress = false
                        if (response.isSuccessful()) {
                            logBf("geocodeAddress: ${response.body()}")

                            val data = obtenerReverseGeoObject(response)
                            if (data != null) {
                                val number = data.address!!.houseNumber ?: "-"
                                val finalAddress = number + ", " + data.address!!.road + ", " +
                                        data.address!!.suburb + ", " + data.address!!.town
                                onResult(if (number == "-") finalAddress else "")
                            } else {
                                onResult(null)
                            }

                        }

                    }

                    override fun onFailure(call: Call<String?>, t: Throwable) {
                        logBf(
                            "geocodeAddress: error - ${call.request()} , ${t.localizedMessage}"
                        )
                        geocodeAddressRequestInProgress = false
                    }
                })


            } catch (e: Exception) {
                e.printStackTrace()
                onResult(null)
                geocodeAddressRequestInProgress = false
            }
        }
    }

    fun obtenerRuta(
        context: Context,
        origen: LatLng,
        destinoLocation: LatLng,
        onResult: (List<GeoPoint>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                if (SharedPrefsUtil.get(GET_RUTA_ACTIVE_KEY, 0) == 0) {
                    val retrofit = Retrofit.Builder()
                        .baseUrl(GEO_ROUTE_ADD)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    SharedPrefsUtil.set(GET_RUTA_ACTIVE_KEY, 1)
                    val service = retrofit.create(OSRMService::class.java)
                    val call = service.obtenerRuta(
                        lon1 = origen!!.longitude, lat1 = origen!!.latitude,
                        lon2 = destinoLocation!!.longitude, lat2 = destinoLocation!!.latitude
                    )
                    logBf("enviando  obtenerRuta - ")
                    call.enqueue(object : Callback<OSRMResponse> {
                        override fun onResponse(
                            call: Call<OSRMResponse>,
                            response: Response<OSRMResponse>
                        ) {
                            if (response.isSuccessful) {
                                val geometry = response.body()?.routes?.get(0)?.geometry
                                if (geometry != null) {
                                    // Decodificar Polyline a una lista de GeoPoint
                                    logBf(" obtenerRuta - ${response.body()?.routes?.isNotEmpty()}")
                                    onResult(decodePolyline(geometry))
                                }
                            } else {
                                logBf(" obtenerRuta - ERROR")
                            }
                            SharedPrefsUtil.set(GET_RUTA_ACTIVE_KEY, 0)
                        }

                        override fun onFailure(call: Call<OSRMResponse>, t: Throwable) {
                            t.printStackTrace()
                            logBf(" obtenerRuta - ${t.printStackTrace()}")
                            SharedPrefsUtil.set(GET_RUTA_ACTIVE_KEY, 0)
                        }
                    })
                } else {
                    logBf("obtenerRuta: esperando respuesta")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                onResult(null)
                SharedPrefsUtil.set(GET_RUTA_ACTIVE_KEY, 0)
            }
        }
    }

    private fun obtenerReverseGeoObject(response: Response<String?>): ReverseGeoObject {
        val str = response.body().toString()
        val gson = Gson()
        val movilData: ReverseGeoObject =
            gson.fromJson(str, ReverseGeoObject::class.java)
        return movilData
    }

    private fun obtenerGeoObject(response: Response<String?>): List<GeoObject> {
        val str = response.body().toString()
        val gson = Gson()
        val movilData: List<GeoObject> =
            gson.fromJson(str, Array<GeoObject>::class.java).toList()
        return movilData
    }

    private fun logBf(text: String) {
        if (SharedPrefsUtil.get(BUGFENDER_KEY, false))
            Bugfender.d(TAG_VIEWMODEL, text)

        Log.w(TAG_VIEWMODEL, text)
    }
}