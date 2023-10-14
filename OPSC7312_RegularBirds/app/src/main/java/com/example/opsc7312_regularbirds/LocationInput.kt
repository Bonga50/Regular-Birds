package com.example.opsc7312_regularbirds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView

class LocationInput : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_input)

        val txtStartLocation = findViewById<EditText>(R.id.txtStartLocation)

        val btnBack:ImageView = findViewById(R.id.btnBackHome)

        txtStartLocation.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }


        btnBack.setOnClickListener{
            onBackPressed()
        }

    }

    private fun performSearch(){
        val intent = Intent(this, BirdNavigationActivity::class.java)
        startActivity(intent)
    }
}