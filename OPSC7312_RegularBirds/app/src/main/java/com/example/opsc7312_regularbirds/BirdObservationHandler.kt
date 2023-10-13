package com.example.opsc7312_regularbirds

object BirdObservationHandler {

    val observations = mutableListOf<BirdObservationModel>(
        BirdObservationModel(1.0, "Observation 1", -77.0369, 38.9072),
        BirdObservationModel(2.0, "Observation 2", -122.4194, 37.7749),
        BirdObservationModel(3.0, "Observation 3", -84.3879, 33.7490),
        BirdObservationModel(4.0, "Observation 4", -71.0589, 42.3601),
        BirdObservationModel(5.0, "Observation 5", -75.1652, 39.9526)
    )
    //Add new Bird observation
    fun addObservation(observation: BirdObservationModel) {
        observations.add(observation)
    }

    // Method to get an observation by its ID
    fun getObservationById(observationId: Double): BirdObservationModel? {
        return observations.find { it.observationId == observationId }
    }

    // Method to update an observation by its ID
    fun updateObservationById(observationId: Double, updatedObservation: BirdObservationModel) {
        val existingObservation = getObservationById(observationId)
        if (existingObservation != null) {
            // Update the observation with the provided data
            existingObservation.observationName = updatedObservation.observationName
            existingObservation.userLocationLongitude = updatedObservation.userLocationLongitude
            existingObservation.userLocationLatitude = updatedObservation.userLocationLatitude
        }
    }


}