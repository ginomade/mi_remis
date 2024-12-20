package com.nomade.miremis.net

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OSRMService {
    @GET("route/v1/driving/{lon1},{lat1};{lon2},{lat2}")
    fun obtenerRuta(
        @Path("lon1") lon1: Double,
        @Path("lat1") lat1: Double,
        @Path("lon2") lon2: Double,
        @Path("lat2") lat2: Double,
        @Query("overview") overview: String = "full"
    ): Call<OSRMResponse>
}