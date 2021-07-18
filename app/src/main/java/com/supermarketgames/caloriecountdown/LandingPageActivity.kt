package com.supermarketgames.caloriecountdown

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class LandingPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Grab values from content view
        setContentView(R.layout.activity_landing_page)
        // val newGameButton: Button = findViewById(R.id.newGameButton)
        val highScoresButton: Button = findViewById(R.id.highScoresButton)
        val settingsButton: FloatingActionButton = findViewById(R.id.settingsFloatingActionButton)

        // Create intents
        val highScoresIntent = Intent(
            this,
            HighScoresActivity::class.java
        )
        val settingsIntent = Intent(
            this,
            SettingsActivity::class.java
        )

        // Set on click listeners
        // New game button
        // newGameButton.setOnClickListener

        // High scores button
        highScoresButton.setOnClickListener {
            startActivity(highScoresIntent)
        }

        // Settings button
        settingsButton.setOnClickListener{
            startActivity(settingsIntent)
        }

    }
}