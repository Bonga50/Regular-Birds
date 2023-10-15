package com.example.opsc7312_regularbirds

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

}