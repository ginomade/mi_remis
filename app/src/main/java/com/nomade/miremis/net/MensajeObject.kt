package com.nomade.miremis.net

import com.google.gson.annotations.SerializedName

data class MensajeObject(
    @SerializedName("horamsj") var horamsj: String? = null,
    @SerializedName("textomsj") var textomsj: String? = null
)
