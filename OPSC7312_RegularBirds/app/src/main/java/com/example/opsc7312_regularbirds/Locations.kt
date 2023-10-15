package com.example.opsc7312_regularbirds

data class Locations(val locId: String,
                     val locName: String,
                     val lat: Double,
                     val lng: Double, // Add distance property
                     val comName: String,
                     val sciName:String,
                     val speciesCode: String,
                     var obsvId:Int
                     )


