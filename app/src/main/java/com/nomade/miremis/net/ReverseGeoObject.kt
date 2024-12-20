package com.nomade.miremis.net

import com.google.gson.annotations.SerializedName

data class ReverseGeoObject(
    @SerializedName("place_id") var placeId: Int? = null,
    @SerializedName("licence") var licence: String? = null,
    @SerializedName("osm_type") var osmType: String? = null,
    @SerializedName("osm_id") var osmId: Double? = null,
    @SerializedName("lat") var lat: String? = null,
    @SerializedName("lon") var lon: String? = null,
    @SerializedName("class") var class_name: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("place_rank") var placeRank: Int? = null,
    @SerializedName("importance") var importance: Double? = null,
    @SerializedName("addresstype") var addresstype: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("display_name") var displayName: String? = null,
    @SerializedName("address") var address: Address? = Address(),
    @SerializedName("boundingbox") var boundingbox: ArrayList<String> = arrayListOf()
)

data class Address(

    @SerializedName("house_number") var houseNumber: String? = null,
    @SerializedName("road") var road: String? = null,
    @SerializedName("suburb") var suburb: String? = null,
    @SerializedName("town") var town: String? = null,
    @SerializedName("municipality") var municipality: String? = null,
    @SerializedName("state_district") var stateDistrict: String? = null,
    @SerializedName("state") var state: String? = null,
    @SerializedName("ISO3166-2-lvl4") var ISO3166: String? = null,
    @SerializedName("postcode") var postcode: String? = null,
    @SerializedName("country") var country: String? = null,
    @SerializedName("country_code") var countryCode: String? = null

)