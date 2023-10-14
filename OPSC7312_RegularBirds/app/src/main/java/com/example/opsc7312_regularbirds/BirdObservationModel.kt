package com.example.opsc7312_regularbirds

import java.io.Serializable

data class BirdObservationModel(
    var observationId:Int
,var observationName: String
,var observationDate: String
,var userLocationLongitude: Double
,var userLocationLatitude: Double
,var UserId:String): Serializable
