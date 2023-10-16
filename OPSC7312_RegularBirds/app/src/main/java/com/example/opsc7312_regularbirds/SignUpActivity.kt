package com.example.opsc7312_regularbirds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class SignUpActivity : AppCompatActivity() {

    var userMod = UserHandler;
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var checkpasswordEditText: EditText
    private lateinit var sumbitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        usernameEditText = findViewById(R.id.txtUsername)
        passwordEditText = findViewById(R.id.txtPassword)
        checkpasswordEditText = findViewById(R.id.txtconfirmPassword)
        sumbitButton = findViewById(R.id.btnSignUp)


        sumbitButton.setOnClickListener{
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val checkpass = checkpasswordEditText.text.toString()
            if(checkpass == password){
                userMod.addElementToList(username, password)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
            }

        }
    }
}