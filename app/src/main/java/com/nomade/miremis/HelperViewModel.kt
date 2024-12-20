package com.nomade.miremis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HelperViewModel : ViewModel() {

    private val estadoViaje = MutableLiveData<Estado>()

    private val _resetMap = MutableStateFlow<Boolean>(false)
    val resetMap: StateFlow<Boolean> = _resetMap

    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation

    private val _destinoLocation = MutableStateFlow<LatLng?>(null)
    val destinoLocation: StateFlow<LatLng?> = _destinoLocation

    private val _origenLocation = MutableStateFlow<LatLng?>(null)
    val origenLocation: StateFlow<LatLng?> = _origenLocation

    private val _cancelPedido = MutableStateFlow<Boolean>(false)
    val cancelPedido: StateFlow<Boolean> = _cancelPedido

    private val _updatePrecio = MutableStateFlow<Boolean>(false)
    val updatePrecio: StateFlow<Boolean> = _updatePrecio

    private val _cancelPedidoConfirmado = MutableStateFlow<Boolean>(false)
    val cancelPedidoConfirmado: StateFlow<Boolean> = _cancelPedidoConfirmado

    private val _currentAddress = MutableStateFlow<String?>(null)
    val currentAddress: StateFlow<String?> = _currentAddress

    fun setNewGeopos(geopos: LatLng) {
        _currentLocation.value = geopos
    }

    fun setCancelPedidoConfirmado(cancel: Boolean) {
        _cancelPedidoConfirmado.value = cancel
    }

    fun setCancelPedido(cancel: Boolean) {
        _cancelPedido.value = cancel
    }

    fun setUpdatePrecio(updated: Boolean) {
        _updatePrecio.value = updated
    }

    fun setOrigenGeopos(geopos: LatLng) {
        _origenLocation.value = geopos
    }

    fun setDestinoGeopos(geopos: LatLng) {
        _destinoLocation.value = geopos
    }

    fun setAddress(geopos: String) {
        _currentAddress.value = geopos
    }

    fun getEstadoViaje(): LiveData<Estado> {
        return estadoViaje
    }

    fun setEstadoViaje(estado: Estado) {
        estadoViaje.postValue(estado)
    }

    fun triggerResetMap() {
        _resetMap.value = true
    }

    fun resetMapComplete() {
        _resetMap.value = false
    }
}

enum class Estado {
    INICIO, CON_ORIGEN, PENDIENTE, ASIGNADO, RECHAZADO, EN_CURSO, FINALIZADO, ERROR, SIN_DATOS
}