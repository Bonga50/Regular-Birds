package com.example.opsc7312_regularbirds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.opsc7312_regularbirds.UserHandler
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpTextView: TextView

    var userMod = UserHandler;

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        firebaseAuth = Firebase.auth
        usernameEditText = findViewById(R.id.txtUsername)
        passwordEditText = findViewById(R.id.txtPassword)
        loginButton = findViewById(R.id.btnLogin)
        signUpTextView = findViewById(R.id.txtSignUp)



        loginButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)

            var username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            username = UserHandler.removeWhitespaces(username)

            //val user = UserHandler.getUserByUsername(username)
            firebaseAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener {
                // authentication performed here
                if (it.isSuccessful) {
                    // the home class is yet to be created so the error will be there
                    // seeing as the home activity class has not been created yet, when logging in, it will just display
                    // failed or successful message.
                    UserHandler.setVerifiedUser(username)
                    val intent = Intent(this, HomeActivity::class.java)

                    startActivity(intent)
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                } else {

                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()

                }
            }

        }

        signUpTextView.setOnClickListener{

            val intent = Intent(this, SignUpActivity::class.java)

            startActivity(intent)
        }




        }

    }









