package com.iie.opsc7312_regularbirds

import java.io.Serializable

data class BirdObservationModel(
    var observationId:String=""
,var observationName: String=""
,var observationDate: String=""
,var userLocationLongitude: Double=0.0
,var userLocationLatitude: Double=0.0
,var UserId:String=""
,var imageData: String? = ""
): Serializable
