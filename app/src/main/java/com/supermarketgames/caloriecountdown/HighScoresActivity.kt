package com.supermarketgames.caloriecountdown

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HighScoresActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Grab values from content view
        setContentView(R.layout.activity_high_scores)
        val returnButton: FloatingActionButton = findViewById(
            R.id.highScoresReturnFloatingActionButton
        )

        // Create intents
        val landingPageIntent = Intent(
            this,
            LandingPageActivity::class.java
        )

        // Set on click listeners
        returnButton.setOnClickListener {
            startActivity(landingPageIntent)
        }

    }
}