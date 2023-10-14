package com.example.opsc7312_regularbirds

object BirdObservationHandler {

    val observations = mutableListOf<BirdObservationModel>(
        BirdObservationModel(1, "Observation 1","10/14/2023",-77.0369, 38.9072,"User1"),
        BirdObservationModel(2, "Observation 2","10/14/2023",-122.4194, 37.7749,"User1"),
        BirdObservationModel(3, "Observation 3","10/14/2023",-84.3879, 33.7490,"User1"),
        BirdObservationModel(4, "Observation 4","10/14/2023",-71.0589, 42.3601,"User1"),
        BirdObservationModel(5, "Observation 5","10/14/2023",-75.1652, 39.9526,"User1")
    )
    //Add new Bird observation
    fun addObservation(observation: BirdObservationModel) {
        observations.add(observation)
    }

    // Method to get an observation by its ID
    fun getObservationById(observationId: Int): BirdObservationModel? {
        return observations.find { it.observationId == observationId }
    }

    // Method to update an observation by its ID
    fun updateObservationById(observationId: Int, updatedObservation: BirdObservationModel) {
        val existingObservation = getObservationById(observationId)
        if (existingObservation != null) {
            // Update the observation with the provided data
            existingObservation.observationName = updatedObservation.observationName
            existingObservation.userLocationLongitude = updatedObservation.userLocationLongitude
            existingObservation.userLocationLatitude = updatedObservation.userLocationLatitude
        }
    }


}