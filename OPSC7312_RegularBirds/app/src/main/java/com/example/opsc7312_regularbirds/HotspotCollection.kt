package com.example.opsc7312_regularbirds

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface HotspotCollection {
    @GET("data/obs/geo/recent")
    fun getHotspots(
        @Query("key") apiKey: String,
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("dist") distance: Int // Search radius in kilometers

    ): Call<List<Locations>>
}

/*interface HotspotCollection {
    @GET("https://api.ebird.org/v2/data/obs/geo/recent?key=6p4jn23qffqh&lat=-26.084&lng=28.14")
    fun getHotspots(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("dist") distance: Int, // Search radius in kilometers
        @Query("key") apiKey: String
    ): Call<List<Locations>>
}*/
