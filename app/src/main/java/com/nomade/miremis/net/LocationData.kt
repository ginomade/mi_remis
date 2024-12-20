package com.nomade.miremis.net

import com.google.gson.annotations.SerializedName


data class LocationData (

    @SerializedName("data" ) var data : ArrayList<LocationElement> = arrayListOf()

)