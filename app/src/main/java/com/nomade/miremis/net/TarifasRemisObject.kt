package com.nomade.miremis.net

import com.google.gson.annotations.SerializedName

data class TarifasRemisObject(
    @SerializedName("ZonaI") var ZonaI: Int? = null,
    @SerializedName("ZonaH") var ZonaH: Int? = null,
    @SerializedName("Numeral") var Numeral: Int? = null
)
