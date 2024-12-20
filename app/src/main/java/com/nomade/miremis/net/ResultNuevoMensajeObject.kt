package com.nomade.miremis.net

import com.google.gson.annotations.SerializedName

data class ResultNuevoMensajeObject (
    @SerializedName("mensaje"  ) var mensaje  : String?    = null
)
