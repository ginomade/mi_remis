package com.nomade.miremis.net

import com.google.gson.annotations.SerializedName

data class ViajeObject(
    @SerializedName("Fecha") var fecha: String? = null,
    @SerializedName("Hora") var hora: String? = null,
    @SerializedName("IdMovil") var idMovil: Int? = null,
    @SerializedName("Direccion") var direccion: String? = null,
    @SerializedName("Observaciones") var observaciones: String? = null
)
