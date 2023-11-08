package com.example.opsc7312_regularbirds

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CompletableDeferred

object UserHandler {

    private val users = mutableListOf<UserModel>(
        UserModel("User1", "Password1"),
        UserModel("User2", "Password2"),
        UserModel("User3", "Password3")
    )

    var userSettingsDefault:settingsModel = settingsModel("","",10)
    private var verifiedUser: String? = "User1"

    fun addUser(user: UserModel) {
        users.add(user)
    }

    fun addElementToList(username: String, password: String) {
        users.add(UserModel(username, password))
    }

    /*fun getUserByUsername(username: String, password): UserModel? {

        var condition : Boolean;

        users.forEach{element ->
            if(element.username == username &&
                element.password == password) {
                condition = true;

            }
        }
    }*/
    fun getUserByUsername(username: String): UserModel? {
        return users.find { it.username == username }
    }

    fun checkPassword(username: String, password: String): Boolean {
        val user = getUserByUsername(username)
        return user?.password == password
    }

    fun getVerifiedUser(): String? {
        return verifiedUser
    }

    fun setVerifiedUser(username: String) {
        this.verifiedUser = username
    }
    fun removeWhitespaces(input: String): String {
        return input.replace("\\s".toRegex(), "")
    }
    //method to add user settings
    fun addsettingstoFireBase(userSettingsDefault:settingsModel){
        val db = com.google.firebase.Firebase.firestore
        db.collection("userSettings")
        .add(userSettingsDefault)
    }

    //method to get user settings
    suspend fun getSettingsFromFirebse():settingsModel{
        val db = com.google.firebase.Firebase.firestore
        val deferred = CompletableDeferred<String>()
        val settings = mutableListOf<settingsModel>()
        db.collection("userSettings")
            .whereEqualTo("userId", UserHandler.getVerifiedUser())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var settingsModel = document.toObject(settingsModel::class.java)
                    settings.add(settingsModel)
                }

                deferred.complete("Success")
            }
            .addOnFailureListener { exception ->
                Log.w(Popup_hotspotdetailsFragment.TAG, "Error getting documents: ", exception)
                deferred.complete("Failed")
            }
        val result = deferred.await()
        Log.d("Success", "Error getting documents: " + result.toString())
        userSettingsDefault = settings.first()
        return userSettingsDefault
    }
    //method to update user UnitOfmeasurment
    fun updateUnitOfMeasurmentInFirebase(userSettingsUnit: String) {
        val db = com.google.firebase.Firebase.firestore

        // Query the database for the document with the specific userId
        db.collection("userSettings")
            .whereEqualTo("userId", UserHandler.getVerifiedUser())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Get a reference to the document you want to update
                    val docRef = db.collection("userSettings").document(document.id)

                    // Create a map with the field you want to update
                    val updates = hashMapOf<String, Any>(
                        "UnitOfMeasurment" to userSettingsUnit
                    )

                    // Update the document
                    docRef.update(updates)
                        .addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot successfully updated!")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error updating document", e)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    fun updateDistanceInFirebase(userSettingsDist: Int) {
        val db = com.google.firebase.Firebase.firestore

        // Query the database for the document with the specific userId
        db.collection("userSettings")
            .whereEqualTo("userId", UserHandler.getVerifiedUser())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Get a reference to the document you want to update
                    val docRef = db.collection("userSettings").document(document.id)

                    // Create a map with the field you want to update
                    val updates = hashMapOf<String, Any>(
                        "Distance" to userSettingsDist
                    )

                    // Update the document
                    docRef.update(updates)
                        .addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot successfully updated!")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error updating document", e)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }


}