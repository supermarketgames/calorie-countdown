package com.supermarketgames.caloriecountdown

import android.content.Intent
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Grab values from content view
        setContentView(R.layout.activity_settings)
        val returnButton: FloatingActionButton = findViewById(
            R.id.settingsReturnFloatingActionButton
        )
        val darkModeSwitch: Switch = findViewById(
            R.id.darkModeSwitch
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
        darkModeSwitch.setOnClickListener {
            if (darkModeSwitch.isChecked) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            }
        }
    }
}