package com.example.opsc7312_regularbirds

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Hotspots {
    private const val BASE_URL = "https://api.ebird.org/v2/"

    fun createEBirdService():  HotspotCollection{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(HotspotCollection::class.java)
    }

}