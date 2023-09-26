package com.example.opsc7312_regularbirds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        usernameEditText = findViewById(R.id.txtUsername)
        passwordEditText = findViewById(R.id.txtPassword)
        loginButton = findViewById(R.id.btnLogin)



        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            // authentication performed here
            if (isValidCredentials(username, password)) {
                // the home class is yet to be created so the error will be there
                // seeing as the home activity class has not been created yet, when logging in, it will just display
                // failed or successful message.
                val intent = Intent(this, HomeActivity::class.java)

                startActivity(intent)
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()

            }


        }




        }
    private fun isValidCredentials(username: String, password: String): Boolean {
        // For testing purposes, let's use test credentials "testuser" and "testpassword"
        return username == "testUser" && password == "testPassword"
    }
    }









