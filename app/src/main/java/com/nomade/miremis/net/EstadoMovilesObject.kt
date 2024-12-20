package com.nomade.miremis.net

import com.google.gson.annotations.SerializedName

data class EstadoMovilesObject(
    @SerializedName("Moviles"     ) var Moviles     : Int?    = null,
    @SerializedName("Coordenadas" ) var Coordenadas : String? = null
)
