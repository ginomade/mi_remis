package com.nomade.miremis.net

import com.google.gson.annotations.SerializedName

data class GeoObject(
    @SerializedName("place_id") var placeId: Int? = null,
    @SerializedName("licence") var licence: String? = null,
    @SerializedName("osm_type") var osmType: String? = null,
    @SerializedName("osm_id") var osmId: Double? = null,
    @SerializedName("lat") var lat: String? = null,
    @SerializedName("lon") var lon: String? = null,
    @SerializedName("category") var category: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("place_rank") var placeRank: Int? = null,
    @SerializedName("importance") var importance: Double? = null,
    @SerializedName("addresstype") var addresstype: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("display_name") var displayName: String? = null,
    @SerializedName("address") var address: Address? = Address(),
    @SerializedName("boundingbox") var boundingbox: ArrayList<String> = arrayListOf()
)
