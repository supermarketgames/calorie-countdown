package com.supermarketgames.caloriecountdown

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.Instant

class BaseGameActivity : AppCompatActivity() {

    val TIME_LIMIT_SECONDS = 60
    val NUMBER_XS_FOR_GAME_OVER = 3

    var gameStartTime = Instant.now()
    var pointCount = 0
    var numberXsReceived = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Grab values from content view
        setContentView(R.layout.activity_game)
        val returnButton: FloatingActionButton = findViewById(
            R.id.gameReturnFloatingActionButton
        )
        val timerTextView: TextView = findViewById(
            R.id.timerTextView
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