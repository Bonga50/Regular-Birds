package com.example.opsc7312_regularbirds

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var userMod = UserHandler;
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var checkpasswordEditText: EditText
    private lateinit var sumbitButton: Button
    private lateinit var signinButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        usernameEditText = findViewById(R.id.txtUsername)
        passwordEditText = findViewById(R.id.txtPassword)
        checkpasswordEditText = findViewById(R.id.txtconfirmPassword)
        sumbitButton = findViewById(R.id.btnSignUp)
        signinButton = findViewById(R.id.txtSignIn)


        sumbitButton.setOnClickListener{
            var username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val checkpass = checkpasswordEditText.text.toString()
            if(checkpass == password){
//                userMod.addElementToList(username, password)
                username = UserHandler.removeWhitespaces(username)

                auth.createUserWithEmailAndPassword(username,password)
                    .addOnCompleteListener{
                        if (it.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(ContentValues.TAG, "createUserWithEmail:success")

                            val user = auth.currentUser
                            UserHandler.addsettingstoFireBase(settingsModel(username,"Metric",10))
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(ContentValues.TAG, "createUserWithEmail:failure", it.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }
            else{
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
            }
            signinButton.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}