package com.nomade.miremis.net

import com.google.gson.annotations.SerializedName

data class TarifasObject(
    @SerializedName("bajada") var bajada: String? = null,
    @SerializedName("ficha") var ficha: String? = null,
    @SerializedName("tipo") var tipo: String? = null
)
