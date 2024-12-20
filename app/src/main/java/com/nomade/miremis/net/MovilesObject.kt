package com.nomade.miremis.net

import com.google.gson.annotations.SerializedName

data class MovilesObject(
    @SerializedName("Movil") var movil: String? = null,
    @SerializedName("CoorLibre") var coorLibre: String? = null,
    @SerializedName("Distance") var distance: Double? = null
)
