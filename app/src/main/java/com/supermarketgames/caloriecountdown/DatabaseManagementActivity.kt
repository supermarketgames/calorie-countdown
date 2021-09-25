package com.supermarketgames.caloriecountdown

import android.content.Intent
import android.icu.text.NumberFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DatabaseManagementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Grab values from content view
        setContentView(R.layout.activity_database_management)

        // Text fields
        val foodIdTextEdit: TextView = findViewById(
            R.id.foodIdTextEdit
        )
        val foodNameTextEdit: TextView = findViewById(
            R.id.foodNameTextEdit
        )
        val caloriesTextEdit: TextView = findViewById(
            R.id.caloriesTextEdit
        )

        // Buttons
        val returnButton: FloatingActionButton = findViewById(
            R.id.databaseManagementReturnFloatingActionButton
        )
        val addFoodButton: Button = findViewById(
            R.id.addFoodButton
        )
        val getCaloriesButton: Button = findViewById(
            R.id.getCaloriesButton
        )
        val removeFoodIdButton: Button = findViewById(
            R.id.removeFoodIdButton
        )

        // Create intents
        val settingsPageIntent = Intent(
            this,
            SettingsActivity::class.java
        )

        // Initiate database
        val foodDatabaseHelper: FoodDatabaseHelper = FoodDatabaseHelper(this)

        // Set on click listeners
        returnButton.setOnClickListener {
            startActivity(settingsPageIntent)
        }

        addFoodButton.setOnClickListener {
            val output: Int = foodDatabaseHelper.setRow(
                foodId=foodIdTextEdit.text.toString().toInt(),
                foodName=foodNameTextEdit.text.toString(),
                foodCalories=caloriesTextEdit.text.toString().toDouble()
            )
            if (output != 0) {
                val errorMessage: String = "ERROR: FOOD ID ALREADY EXISTS"
                foodNameTextEdit.text = errorMessage
            }
        }

        val numberFormatter: NumberFormat = NumberFormat.getInstance()
        getCaloriesButton.setOnClickListener {
            val output: Double? = foodDatabaseHelper.getCalories(
                foodId=foodIdTextEdit.text.toString().toInt(),
            )
            caloriesTextEdit.text = numberFormatter.format(output)
        }

        removeFoodIdButton.setOnClickListener {
            val output: Int? = foodDatabaseHelper.removeRow(
                foodId=foodIdTextEdit.text.toString().toInt()
            )
            if (output != -1) {
                val outputMessage: String = "$output : Food removed"
                foodNameTextEdit.text = outputMessage
            }
        }
    }
}