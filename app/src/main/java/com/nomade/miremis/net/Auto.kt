package com.nomade.miremis.net

import com.google.gson.annotations.SerializedName

data class Auto (

    @SerializedName("Movil"   ) var movil   : String? = null,
    @SerializedName("Marca"   ) var marca   : String? = null,
    @SerializedName("Modelo"  ) var modelo  : String? = null,
    @SerializedName("Color"   ) var color   : String? = null,
    @SerializedName("Dominio" ) var dominio : String? = null

)