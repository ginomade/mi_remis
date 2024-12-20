package com.nomade.miremis

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bugfender.sdk.Bugfender
import com.google.gson.Gson
import com.nomade.miremis.net.EstadoMovilesObject
import com.nomade.miremis.net.MensajeObject
import com.nomade.miremis.net.MovilesObject
import com.nomade.miremis.net.ResultNuevoMensajeObject
import com.nomade.miremis.net.ResultObject
import com.nomade.miremis.net.ResultPedido
import com.nomade.miremis.net.RetrofitService
import com.nomade.miremis.net.TarifasObject
import com.nomade.miremis.net.TarifasRemisObject
import com.nomade.miremis.net.ViajeObject
import com.nomade.miremis.utils.Constants.BUGFENDER_KEY
import com.nomade.miremis.utils.Constants.WAITING_STATE_KEY
import com.nomade.miremis.utils.SharedPrefsUtil
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MainViewModel : ViewModel() {
    private val userInfo = MutableLiveData<String>()
    private val userStatus = MutableLiveData<String>()
    private val resultMensaje = MutableLiveData<String>()
    private val resultListMensajes = MutableLiveData<List<MensajeObject>>()
    private val resultListViajes = MutableLiveData<List<ViajeObject>>()
    private val resultMoviles = MutableLiveData<List<MovilesObject>>()
    private val resultNuevoMensaje = MutableLiveData<ResultNuevoMensajeObject>()
    private val resultEstadoMovil = MutableLiveData<List<EstadoMovilesObject>>()
    private val resultPedido = MutableLiveData<ResultPedido>()
    private val resultCancel = MutableLiveData<Boolean>()
    private val resultUltimoPedido = MutableLiveData<ResultPedido>()
    private val resultTarifas = MutableLiveData<TarifasObject>()
    private val resultTarifaRemis = MutableLiveData<TarifasRemisObject>()
    private val resultReclamo = MutableLiveData<String>()

    private var buscarHistorialViajesRequestInProgress = false
    private var leerHistoricoMensajesRequestInProgress = false
    private var buscarMovilesRequestInProgress = false



    private val _loadingState = MutableStateFlow<Boolean>(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    fun triggerLoading() {
        _loadingState.value = true
    }

    fun stopLoading() {
        _loadingState.value = false
    }


    private var stateRunning = false
    private var stateJob: Job? = null

    var TAG_VIEWMODEL = "MainViewModel"

    override fun onCleared() {
        super.onCleared()
        stateJob?.cancel()
        stateRunning = false
    }

    fun getUserInfo(): LiveData<String> {
        return userInfo
    }

    fun getPedidoResponse(): LiveData<ResultPedido> {
        return resultPedido
    }

    fun getCancelResponse(): LiveData<Boolean> {
        return resultCancel
    }

    fun setCompletedCancelResponse() {
        resultCancel.postValue(false)
    }

    fun getUltimoPedidoResponse(): LiveData<ResultPedido> {
        return resultUltimoPedido
    }

    fun getResultHayMensaje(): LiveData<String> {
        return resultMensaje
    }

    fun getResultReclamo(): LiveData<String> {
        return resultReclamo
    }

    fun getResultEstadoMovil(): LiveData<List<EstadoMovilesObject>> {
        return resultEstadoMovil
    }

    fun getResultMoviles(): LiveData<List<MovilesObject>> {
        return resultMoviles
    }

    fun getResultTarifas(): LiveData<TarifasObject> {
        return resultTarifas
    }

    fun getResultTarifaRemis(): LiveData<TarifasRemisObject> {
        return resultTarifaRemis
    }

    fun getResultNuevoMensaje(): LiveData<ResultNuevoMensajeObject> {
        return resultNuevoMensaje
    }

    fun getMensajes(): LiveData<List<MensajeObject>> {
        return resultListMensajes
    }

    fun getViajes(): LiveData<List<ViajeObject>> {
        return resultListViajes
    }


    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logBf("Exception handled: ${throwable.localizedMessage}")
    }

    fun buscarHistorialViajes(userEmail: String) {
        if (buscarHistorialViajesRequestInProgress) {
            return
        }

        buscarHistorialViajesRequestInProgress = true
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                logBf("enviando - buscarHistorialViajes ${userEmail}")
                val call: Call<String?>? =
                    RetrofitService.RetrofitInstance.api.buscarHistorialViajes(userEmail)
                call!!.enqueue(object : Callback<String?> {
                    override fun onResponse(
                        call: Call<String?>,
                        response: Response<String?>
                    ) {
                        buscarHistorialViajesRequestInProgress = false
                        if (response.isSuccessful()) {
                            logBf("buscarHistorialViajes: ${response.body()}")
                            val viajes = obtenerViajesObject(response)
                            resultListViajes.postValue(viajes)
                        }

                    }

                    override fun onFailure(call: Call<String?>, t: Throwable) {
                        buscarHistorialViajesRequestInProgress = false
                        logBf(
                            "buscarHistorialViajes: error - ${call.request()} , ${t.localizedMessage}"
                        )
                    }
                })

            } catch (e: Exception) {
                buscarHistorialViajesRequestInProgress = false
                logBf("buscarHistorialViajes: ${e}")
            }
        }

    }

    fun hayMensajes(userEmail: String, reserva: String) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                logBf("enviando - hayMensajes ${userEmail} ${reserva}")
                val call: Call<String?>? =
                    RetrofitService.RetrofitInstance.api.hayMensajes(userEmail, reserva)
                call!!.enqueue(object : Callback<String?> {
                    override fun onResponse(
                        call: Call<String?>,
                        response: Response<String?>
                    ) {
                        if (response.isSuccessful()) {
                            logBf("hayMensajes: ${response.body()}")
                            var movilData = response.body() ?: ""
                            resultMensaje.postValue(movilData)
                        }

                    }

                    override fun onFailure(call: Call<String?>, t: Throwable) {
                        logBf(
                            "hayMensajes: error - ${call.request()} , ${t.localizedMessage}"
                        )
                    }
                })

            } catch (e: Exception) {
                logBf("hayMensajes: ${e}")
            }
        }

    }

    fun leerMensajes(userEmail: String, reserva: String) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                logBf("enviando - leerMensajes ${userEmail} ${reserva}")
                val call: Call<String?>? =
                    RetrofitService.RetrofitInstance.api.leerMensajes(userEmail, reserva)
                call!!.enqueue(object : Callback<String?> {
                    override fun onResponse(
                        call: Call<String?>,
                        response: Response<String?>
                    ) {
                        if (response.isSuccessful()) {
                            logBf("leerMensajes: ${response.body()}")
                            val mensajes = obtenerMensajesObject(response)
                            resultListMensajes.postValue(mensajes)
                        }

                    }

                    override fun onFailure(call: Call<String?>, t: Throwable) {
                        logBf(
                            "leerMensajes: error - ${call.request()} , ${t.localizedMessage}"
                        )
                    }
                })

            } catch (e: Exception) {
                logBf("leerMensajes: ${e}")
            }
        }

    }

    fun leerHistoricoMensajes(userEmail: String, reserva: String) {
        if (leerHistoricoMensajesRequestInProgress) {
            return
        }

        leerHistoricoMensajesRequestInProgress = true
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                logBf("enviando - leerHistoricoMensajes ${userEmail}")
                val call: Call<String?>? =
                    RetrofitService.RetrofitInstance.api.buscarHistorialMensajes(userEmail, reserva)
                call!!.enqueue(object : Callback<String?> {
                    override fun onResponse(
                        call: Call<String?>,
                        response: Response<String?>
                    ) {
                        leerHistoricoMensajesRequestInProgress = false
                        if (response.isSuccessful()) {
                            logBf("leerHistoricoMensajes: ${response.body()}")
                            if (response.body()!!.lowercase().startsWith("[\"mensaje\":")
                                || response.body()!!.lowercase().startsWith("{\"mensaje\":")) {
                                // no hay mensajes
                            } else {
                                val mensajes = obtenerMensajesObject(response)
                                resultListMensajes.postValue(mensajes)
                            }
                        }
                    }

                    override fun onFailure(call: Call<String?>, t: Throwable) {
                        leerHistoricoMensajesRequestInProgress = false
                        logBf(
                            "leerHistoricoMensajes: error - ${call.request()} , ${t.localizedMessage}"
                        )
                    }
                })

            } catch (e: Exception) {
                leerHistoricoMensajesRequestInProgress = false
                logBf("leerHistoricoMensajes: ${e}")
            }
        }

    }

    fun enviarMensaje(userEmail: String, reserva: String, geo: String, mensaje: String) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                logBf("enviando - enviarMensaje ${userEmail}")
                val call: Call<String?>? =
                    RetrofitService.RetrofitInstance.api.enviarMensajes(
                        userEmail,
                        reserva,
                        geo,
                        mensaje
                    )
                call!!.enqueue(object : Callback<String?> {
                    override fun onResponse(
                        call: Call<String?>,
                        response: Response<String?>
                    ) {
                        if (response.isSuccessful()) {
                            logBf("enviarMensaje: ${response.body()}")
                            val result = obtenerNuevoMensajeObject(response)
                            resultNuevoMensaje.postValue(result)
                        }

                    }

                    override fun onFailure(call: Call<String?>, t: Throwable) {
                        logBf(
                            "enviarMensaje: error - ${call.request()} , ${t.localizedMessage}"
                        )
                    }
                })

            } catch (e: Exception) {
                logBf("enviarMensaje: ${e}")
            }
        }

    }

    fun buscarMoviles(userEmail: String, usergeopos: String) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                logBf("enviando - buscarMoviles ${userEmail}")

                if(!buscarMovilesRequestInProgress) {
                    buscarMovilesRequestInProgress = true
                    val call: Call<String?>? =
                        RetrofitService.RetrofitInstance.api.buscarMoviles(userEmail, usergeopos)
                    call!!.enqueue(object : Callback<String?> {
                        override fun onResponse(
                            call: Call<String?>,
                            response: Response<String?>
                        ) {
                            if (response.isSuccessful() && isJsonResponse(response)) {
                                logBf("buscarMoviles: ${response.body()}")
                                val movilData = obtenerMovilesObject(response)
                                resultMoviles.postValue(movilData)
                            }
                            buscarMovilesRequestInProgress = false
                        }

                        override fun onFailure(call: Call<String?>, t: Throwable) {
                            logBf("buscarMoviles: error - ${call.request()} , ${t.localizedMessage}")
                            buscarMovilesRequestInProgress = false
                        }
                    })
                } else {

                }

            } catch (e: Exception) {
                logBf("buscarMoviles: ${e}")
                buscarMovilesRequestInProgress = false
            }
        }

    }

    //ultimo viaje del user por email
    fun buscarUltimoViaje(userEmail: String) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                logBf("enviando - buscarUltimoViaje ${userEmail}")
                triggerLoading()
                SharedPrefsUtil.set(WAITING_STATE_KEY, true)
                val call: Call<String?>? =
                    RetrofitService.RetrofitInstance.api.buscarMovilViaje(userEmail)
                call!!.enqueue(object : Callback<String?> {
                    override fun onResponse(
                        call: Call<String?>,
                        response: Response<String?>
                    ) {
                        if (response.isSuccessful() && isJsonResponse(response)) {
                            logBf("buscarUltimoViaje: ${response.body()}")

                            val responsePedido = obtenerPedidoObject(response)
                            resultUltimoPedido.postValue(responsePedido)
                            /*if(!responsePedido.reserva.isNullOrEmpty() && responsePedido.auto.isNullOrEmpty() && responsePedido.idMovil.isNullOrEmpty()) {
                                responsePedido.status = "reset"
                                resultUltimoPedido.postValue(responsePedido)
                            } else {
                                resultUltimoPedido.postValue(responsePedido)
                            }*/
                        }
                        stopLoading()
                        SharedPrefsUtil.set(WAITING_STATE_KEY, false)
                    }

                    override fun onFailure(call: Call<String?>, t: Throwable) {
                        logBf(
                            "buscarUltimoViaje: error - ${call.request()} , ${t.localizedMessage}"
                        )
                        SharedPrefsUtil.set(WAITING_STATE_KEY, false)
                    }
                })

            } catch (e: Exception) {
                logBf("buscarUltimoViaje: ${e}")
                SharedPrefsUtil.set(WAITING_STATE_KEY, false)
            }
        }

    }

    fun enviarReclamo(userEmail: String, celular: String, mensaje: String, usuario: String) {

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                logBf("enviando - enviarReclamo ${userEmail}")
                val call: Call<String?>? =
                    RetrofitService.RetrofitInstance.api.enviarReclamo(
                        userEmail,
                        celular,
                        mensaje,
                        usuario
                    )
                call!!.enqueue(object : Callback<String?> {
                    override fun onResponse(
                        call: Call<String?>,
                        response: Response<String?>
                    ) {
                        if (response.isSuccessful()) {
                            logBf("enviarReclamo: ${response.body()}")
                            resultReclamo.postValue(response.body())
                        }

                    }

                    override fun onFailure(call: Call<String?>, t: Throwable) {
                        logBf(
                            "enviarReclamo: error - ${call.request()} , ${t.localizedMessage}"
                        )
                    }
                })

            } catch (e: Exception) {
                logBf("enviarReclamo: ${e}")
            }
        }

    }

    //genera pedido viaje
    fun enviarPedido(
        celu: String,
        email: String,
        origen: String,
        destino: String,
        usergeopos: String,
        observaciones: String,
        pasajero: String,
        dni: String,
        tarjeta: Boolean
    ) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val date = getCurrentDate()
                val time = getCurrentTime()
                triggerLoading()
                logBf("enviando - enviarPedido ${celu}, ${email}, ${date}, ${time}, ${origen}, ${destino}, ${usergeopos}, ${observaciones}, ${pasajero}, ${dni}, ${tarjeta.toString()}")
                val call: Call<String?>? =
                    RetrofitService.RetrofitInstance.api.enviarPedido(
                        celu,
                        email,
                        date,
                        time,
                        origen,
                        destino,
                        usergeopos,
                        observaciones, pasajero, dni, tarjeta.toString()
                    )
                call!!.enqueue(object : Callback<String?> {
                    override fun onResponse(
                        call: Call<String?>,
                        response: Response<String?>
                    ) {
                        if (response.isSuccessful() && isJsonResponse(response)) {
                            logBf("enviarPedido: ${response.body()}")
                            val responsePedido = obtenerPedidoObject(response)
                            resultPedido.postValue(responsePedido)
                        } else {
                            resultPedido.postValue(ResultPedido(status = "error", message = ""))
                        }
                        stopLoading()
                    }

                    override fun onFailure(call: Call<String?>, t: Throwable) {
                        logBf(
                            "enviarPedido: error - ${call.request()} , ${t.localizedMessage}"
                        )
                        stopLoading()
                    }
                })

            } catch (e: Exception) {
                logBf("enviarPedido: ${e}")
                stopLoading()
            }
        }

    }

    fun getCurrentDate(): String {
        return LocalDate.now().toString()
    }

    fun cancelarPedido(
        reserva: String,
        email: String,
        celu: String
    ) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                logBf("enviando - cancelarPedido ${email}, reserva: ${reserva}")
                val call: Call<String?>? =
                    RetrofitService.RetrofitInstance.api.cancelarPedido(
                        reserva,
                        email,
                        celu
                    )
                call!!.enqueue(object : Callback<String?> {
                    override fun onResponse(
                        call: Call<String?>,
                        response: Response<String?>
                    ) {
                        if (response.isSuccessful()) {
                            logBf("cancelarPedido: ${response.body()}")
                            resultCancel.postValue(true)
                        }

                    }

                    override fun onFailure(call: Call<String?>, t: Throwable) {
                        logBf(
                            "cancelarPedido: error - ${call.request()} , ${t.localizedMessage}"
                        )
                    }
                })

            } catch (e: Exception) {
                logBf("cancelarPedido: ${e}")
            }
        }

    }

    fun getCurrentTime(): String {
        val currentTime = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        return currentTime.format(formatter)
    }

    //con usermovil y usergeopos devuelve posicion movil y distancia
    fun buscarEstadoMovil(userEmail: String, movil: String, usergeopos: String) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                logBf("enviando - buscarEstadoMovil ${movil}")
                val call: Call<String?>? =
                    RetrofitService.RetrofitInstance.api.buscarEstadoMovil(movil, usergeopos)
                call!!.enqueue(object : Callback<String?> {
                    override fun onResponse(
                        call: Call<String?>,
                        response: Response<String?>
                    ) {
                        if (response.isSuccessful() && isJsonResponse(response)) {
                            logBf("buscarEstadoMovil: ${response.body()}")
                            val responsePedido = obtenerEstadoMoviles(response)
                            resultEstadoMovil.postValue(responsePedido)
                        }

                    }

                    override fun onFailure(call: Call<String?>, t: Throwable) {
                        logBf(
                            "buscarEstadoMovil: error - ${call.request()} , ${t.localizedMessage}"
                        )
                    }
                })

            } catch (e: Exception) {
                logBf("buscarEstadoMovil: ${e}")
            }
        }

    }

    fun buscarTarifas() {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                logBf("enviando - buscarTarifas")

                val call: Call<String?>? =
                    RetrofitService.RetrofitInstance.api.buscarTarifas()
                call!!.enqueue(object : Callback<String?> {
                    override fun onResponse(
                        call: Call<String?>,
                        response: Response<String?>
                    ) {
                        if (response.isSuccessful() && isJsonResponse(response)) {
                            logBf("buscarTarifas: ${response.body()}")
                            if (response.body()!!.length > 5) {
                                val movilData = obtenerTarifaObject(response)
                                resultTarifas.postValue(movilData)
                            }
                        }

                    }

                    override fun onFailure(call: Call<String?>, t: Throwable) {
                        logBf("buscarTarifas: error - ${call.request()} , ${t.localizedMessage}")
                    }
                })

            } catch (e: Exception) {
                logBf("buscarTarifas: ${e}")
            }
        }

    }

    fun buscarTarifaRemis(origen: String, destino: String) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                logBf("enviando - buscarTarifaRemis - ${origen} - ${destino}")

                val call: Call<String?>? =
                    RetrofitService.RetrofitInstance.api.buscarTarifaRemis(origen, destino)
                call!!.enqueue(object : Callback<String?> {
                    override fun onResponse(
                        call: Call<String?>,
                        response: Response<String?>
                    ) {
                        if (response.isSuccessful() && isJsonResponse(response)) {
                            logBf("buscarTarifaRemis: ${response.body()}")
                            if (response.body().toString().contains("mensaje")) {
                                val movilData = TarifasRemisObject(0, 0, 0)
                                resultTarifaRemis.postValue(movilData)
                            } else {
                                val movilData = obtenerTarifaRemisObject(response)
                                resultTarifaRemis.postValue(movilData)
                            }
                        }

                    }

                    override fun onFailure(call: Call<String?>, t: Throwable) {
                        logBf("buscarTarifaRemis: error - ${call.request()} , ${t.localizedMessage}")
                    }
                })

            } catch (e: Exception) {
                logBf("buscarTarifaRemis: ${e}")
            }
        }

    }

    fun getCurrentTimeString(): String {
        // Get current date and time
        val currentDateTime = LocalDateTime.now()

        // Format the current date and time to a string using a specific format
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)

        return formattedDateTime
    }

    private fun obtenerResultObject(response: Response<String?>): ResultObject? {
        val str = response.body().toString()
        val index = str.indexOf("}");
        val jsonResponse = str.substring(0, index + 1)
        val gson = Gson()
        val movilData = gson.fromJson(jsonResponse, ResultObject::class.java)
        return movilData
    }

    private fun obtenerMovilesObject(response: Response<String?>): List<MovilesObject> {
        val str = response.body().toString()
        val gson = Gson()
        val movilData: List<MovilesObject> =
            gson.fromJson(str, Array<MovilesObject>::class.java).toList()
        return movilData
    }

    private fun obtenerTarifaObject(response: Response<String?>): TarifasObject {
        val str = response.body().toString()
        val gson = Gson()
        val movilData: TarifasObject =
            gson.fromJson(str, TarifasObject::class.java)
        return movilData
    }

    private fun obtenerTarifaRemisObject(response: Response<String?>): TarifasRemisObject {
        val str = response.body().toString()
        val gson = Gson()
        val movilData: TarifasRemisObject =
            gson.fromJson(str, TarifasRemisObject::class.java)
        return movilData
    }

    private fun obtenerMensajesObject(response: Response<String?>): List<MensajeObject> {
        val str = response.body().toString()
        val gson = Gson()
        val movilData: List<MensajeObject> =
            gson.fromJson(str, Array<MensajeObject>::class.java).toList()
        if (movilData.size > 20) {
            return movilData.take(20)
        }
        return movilData
    }

    private fun obtenerViajesObject(response: Response<String?>): List<ViajeObject> {
        val str = response.body().toString()
        val gson = Gson()
        val movilData: List<ViajeObject> =
            gson.fromJson(str, Array<ViajeObject>::class.java).toList()
        if (movilData.size > 20) {
            return movilData.take(20)
        }
        return movilData
    }

    private fun obtenerPedidoObject(response: Response<String?>): ResultPedido {
        val str = response.body().toString()
        val gson = Gson()
        val data: ResultPedido = gson.fromJson(str, ResultPedido::class.java)
        return data
    }

    private fun obtenerNuevoMensajeObject(response: Response<String?>): ResultNuevoMensajeObject {
        val str = response.body().toString()
        val gson = Gson()
        val data: ResultNuevoMensajeObject =
            gson.fromJson(str, ResultNuevoMensajeObject::class.java)
        return data
    }

    private fun obtenerEstadoMoviles(response: Response<String?>): List<EstadoMovilesObject> {
        val str = response.body().toString()
        val gson = Gson()
        val data: List<EstadoMovilesObject> =
            gson.fromJson(str, Array<EstadoMovilesObject>::class.java).toList()
        return data
    }

    private fun isJsonResponse(response: Response<String?>): Boolean {
        return response.body().toString().startsWith("{") || response.body().toString()
            .startsWith("[")
    }

    private fun logBf(text: String) {
        if (SharedPrefsUtil.get(BUGFENDER_KEY, false))
            Bugfender.d(TAG_VIEWMODEL, text)

        Log.w(TAG_VIEWMODEL, text)
    }

}

