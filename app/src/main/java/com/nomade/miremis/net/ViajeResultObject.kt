package com.nomade.miremis.net

import com.google.gson.annotations.SerializedName

data class ViajeResultObject(
    @SerializedName("Cubierto") var Cubierto: String? = null,
    @SerializedName("Coordenadas") var coordenadas: String? = null
)
