package com.nomade.miremis.net

import com.google.gson.annotations.SerializedName

data class ResultPedido(
    @SerializedName("status") var status: String? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("Reserva") var reserva : String? = null,
    @SerializedName("IdMovil") var idMovil : String? = null,
    @SerializedName("auto") var auto: ArrayList<Auto>? = arrayListOf()

)
