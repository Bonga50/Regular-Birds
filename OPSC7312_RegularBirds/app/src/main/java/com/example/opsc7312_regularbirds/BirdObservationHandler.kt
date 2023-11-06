package com.example.opsc7312_regularbirds

object BirdObservationHandler {

    val observations = mutableListOf<BirdObservationModel>(
        BirdObservationModel("Obsv1", "Observation 1","10/14/2023",22.0369, -23.9072,"User1"),
        BirdObservationModel("Obsv2", "Observation 2","10/14/2023",28.4194, -22.7749,"User1"),
        BirdObservationModel("Obsv3", "Observation 3","10/14/2023",84.3879, -23.7490,"User1"),
        BirdObservationModel("Obsv4", "Observation 4","10/14/2023",51.0589, -22.3601,"User1"),
        BirdObservationModel("Obsv5", "Observation 5","10/14/2023",35.1652, -22.9526,"User1")
    )
    private var selectedObservation: BirdObservationModel? = null

    //Add new Bird observation
    fun addObservation(observation: BirdObservationModel) {
        observations.add(observation)
    }

    // Method to get an observation by its ID
    fun getObservationById(observationId: String): BirdObservationModel? {
        return observations.find { it.observationId == observationId }
    }

    // Method to update an observation by its ID
    fun updateObservationById(observationId: String, updatedObservation: BirdObservationModel) {
        val existingObservation = getObservationById(observationId)
        if (existingObservation != null) {
            // Update the observation with the provided data
            existingObservation.observationName = updatedObservation.observationName
            existingObservation.userLocationLongitude = updatedObservation.userLocationLongitude
            existingObservation.userLocationLatitude = updatedObservation.userLocationLatitude
        }
    }

    //Method that creates an id based on the number of items in a list
    fun generateObservationId(): String {
        return "Obsv"+(observations.size + 1)
    }

    //this will get the selected obsevation
    fun getSelectedObservation(): BirdObservationModel? {
        return selectedObservation
    }

    //Method to set the selected observation
    fun setSelectedObservation(observation: BirdObservationModel) {
        this.selectedObservation = observation
    }


}