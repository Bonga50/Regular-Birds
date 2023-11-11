package com.iie.opsc7312_regularbirds

import android.util.Log
import com.iie.opsc7312_regularbirds.Popup_hotspotdetailsFragment.Companion.TAG
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CompletableDeferred

object BirdObservationHandler {

    lateinit var userDataX : List<BirdObservationModel>
    lateinit var disticntUserData: BirdObservationModel
    lateinit var imageList: List<entryImages>

    val observations = mutableListOf<BirdObservationModel>(
        BirdObservationModel("Obsv1", "Observation 1","10/14/2023",22.0369, -23.9072,"User1",""),
        BirdObservationModel("Obsv2", "Observation 2","10/14/2023",28.4194, -22.7749,"User1",""),
        BirdObservationModel("Obsv3", "Observation 3","10/14/2023",84.3879, -23.7490,"User1",""),
        BirdObservationModel("Obsv4", "Observation 4","10/14/2023",51.0589, -22.3601,"User1",""),
        BirdObservationModel("Obsv5", "Observation 5","10/14/2023",35.1652, -22.9526,"User1","")
    )
    private var selectedObservation: BirdObservationModel? = null

    //Add new Bird observation
    fun addObservation(observation: BirdObservationModel) {
        observations.add(observation)
    }

    // Method to get an observation by its ID
    fun getObservationById(observationId: String): BirdObservationModel? {
        return userDataX.find { it.observationId == observationId }
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
        return "Obsv"+(userDataX.size+1)
    }

    //function to get images
    fun getEntryImage(entryId: String): entryImages? {
        var uploadedImage: entryImages? = imageList.find { it.EntryId == entryId }
        return uploadedImage
    }

    //this will get the selected obsevation
    fun getSelectedObservation(): BirdObservationModel? {
        return selectedObservation
    }

    //Method to set the selected observation
    fun setSelectedObservation(observation: BirdObservationModel) {
        this.selectedObservation = observation
    }

    //Method to generate an observation id

    //function to create an ID for every entry
    fun generateEntryId(): Int {
        var newId: Int
        newId = observations.size + 100
        return newId
    }


    //method that will add observations to the firebase
    fun addDataCObservationToFirestore(observationValue: BirdObservationModel) {
        val db = Firebase.firestore
        db.collection("Observations")
            .add(observationValue)
        }

    //method to get observations for firebase
    suspend fun getUserObservationsFromFireStore(): BirdObservationModel {
        val db = Firebase.firestore
        val deferred = CompletableDeferred<String>()
        val userData = mutableListOf<BirdObservationModel>()

        var tempDistinctUser:BirdObservationModel = BirdObservationModel()

        db.collection("Observations")
            .whereEqualTo("userId",UserHandler.getVerifiedUser())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var entry = document.toObject(BirdObservationModel::class.java)
                    tempDistinctUser = entry
                    userData.add(entry)
                    Log.d("User Data Success", "User data: " + document.id)
                }

                deferred.complete("Success")
            }
            .addOnFailureListener { exception ->
                Log.d("Error", "Error getting documents: " + exception.toString())
                deferred.complete("Failed")
            }

        val result = deferred.await()
        Log.d("Success", "Getting result: " + result.toString())
        userDataX = userData
        disticntUserData = tempDistinctUser
        return disticntUserData
    }

    //delelte method
    suspend fun deleteUserObservationFromFireStore(observationId: String) {
        val db = Firebase.firestore

        db.collection("Observations")
            .whereEqualTo("userId", UserHandler.getVerifiedUser())
            .whereEqualTo("observationId", observationId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    db.collection("Observations").document(document.id).delete()
                }
            }
            .addOnFailureListener { e ->
                Log.w("Delete Error", "Error deleting document", e)
            }
    }

    //method that will get bird images from the firebase
    suspend fun getImagesFromFireStore(): List<entryImages> {
        val db = Firebase.firestore
        val deferred = CompletableDeferred<String>()
        val images = mutableListOf<entryImages>()
        db.collection("entryImages")
            .whereEqualTo("userId", UserHandler.getVerifiedUser())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var image = document.toObject(entryImages::class.java)
                    images.add(image)
                }

                deferred.complete("Success")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
                deferred.complete("Failed")
            }
        val result = deferred.await()
        Log.d("Success", "Error getting documents: " + result.toString())
        imageList = images
        return images
    }

}