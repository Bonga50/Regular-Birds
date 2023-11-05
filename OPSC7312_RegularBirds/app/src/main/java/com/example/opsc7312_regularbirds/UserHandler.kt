package com.example.opsc7312_regularbirds

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object UserHandler {

    private val users = mutableListOf<UserModel>(
        UserModel("User1", "Password1"),
        UserModel("User2", "Password2"),
        UserModel("User3", "Password3")
    )

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


}