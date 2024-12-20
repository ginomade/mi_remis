package com.nomade.miremis.net

data class OSRMResponse(
    val routes: List<Route>
)

data class Route(
    val geometry: String
)