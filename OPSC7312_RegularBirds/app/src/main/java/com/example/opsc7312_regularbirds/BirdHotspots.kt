package com.example.opsc7312_regularbirds

object BirdHotspots {

    var selectedLocation: Locations? = null
    val locationsList = mutableListOf<Locations>()
    var idCount = 0

    var userLatitude:Double=0.0;
    var userLongitude:Double=0.0;

    var disLatitude:Double=0.0;
    var disLongitude:Double=0.0;

    private var maxDistance: Int = 10
    private var unitOfMeasurement: String = "Metric"

    // Method to add a location to the list
    fun addLocation(location: Locations) {
        locationsList.add(location)
    }

    // Method to add multiple locations to the list
    fun addLocations(locations: List<Locations>) {
        locationsList.addAll(locations)
    }

    //method to clear the whole list
    fun clearLocations() {
        locationsList.clear()
    }

    // Method to get a location by obsvId
    fun getLocationByObsvId(obsvId: Int): Locations? {
        return locationsList.find { it.obsvId == obsvId }
    }

    // Method to increment idCount
    fun generateId(): Int {
        return ++idCount
    }

    // Method to reset idCount to 0
    fun resetIdCount() {
        idCount = 0
    }


    // Method to set selectedLocation by obsvId
    fun setSelectedHotspot(obsvId: Int) {
        selectedLocation = getLocationByObsvId(obsvId)
    }

    // Method to get selectedLocation
    fun obtainSelectedLocation(): Locations? {
        return selectedLocation
    }

    //Method to set the users original location
    fun setUserOriginLocation(latitude: Double, longitude: Double) {
        userLatitude = latitude
        userLongitude = longitude
    }

    fun getUserOriginLocation(): Pair<Double, Double> {
        return Pair(userLongitude, userLatitude)
    }

    fun setDistentOriginLocation(latitude: Double, longitude: Double) {
        disLatitude = latitude
        disLongitude = longitude
    }
    fun getDistenation(): Pair<Double, Double>{
        return Pair(disLongitude, disLatitude)
    }
    fun getMaxDistance(): Int {
        return maxDistance
    }

    fun setMaxDistance(maxDistance: Int) {
        this.maxDistance = maxDistance
    }

    fun getUnitOfMeasurement(): String {
        return unitOfMeasurement
    }

    fun setUnitOfMeasurement(unitOfMeasurement: String) {
        this.unitOfMeasurement = unitOfMeasurement
    }


}
