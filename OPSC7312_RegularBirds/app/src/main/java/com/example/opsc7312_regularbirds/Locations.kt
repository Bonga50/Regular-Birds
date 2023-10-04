package com.example.opsc7312_regularbirds

class Locations {
    constructor(){}
    constructor(longitude: Double, latitude: Double) {
        this.longitude = longitude
        this.latitude = latitude
    }

    var longitude: Double = 0.0
    var latitude: Double = 0.0
}