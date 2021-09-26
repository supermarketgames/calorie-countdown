package com.supermarketgames.caloriecountdown

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.abs

class BaseGameActivity : AppCompatActivity() {

    lateinit var foodDatabaseHelper: FoodDatabaseHelper

    val TIME_LIMIT_MILLISECONDS = 60000L
    val MAX_CALORIE_GUESS_DIFFERENCE = 500 // How far off can the guess be without receiving an 'X'
    val NUMBER_XS_FOR_GAME_OVER = 3

    // defining view variables at class level so different lifecycle states can access them
    lateinit var returnButton: FloatingActionButton
    lateinit var timerTextView: TextView
    lateinit var foodNameTextView: TextView
    lateinit var calorieCountGuessTextView: TextView
    lateinit var caloriePickerSeekBar: SeekBar
    lateinit var xCountValueTextView: TextView
    lateinit var pointsValueTextView: TextView

    var foodItemList: List<FoodItem> = ArrayList()
    lateinit var currentFoodItem: FoodItem

    var pointCount = 0
    var xCount = 0

    enum class CaloriePickerState { PICKING, PICKED }

    var caloriePickerState = CaloriePickerState.PICKING

    enum class CountDownTimerState { STARTED, STOPPED }

    var timerState = CountDownTimerState.STOPPED
    lateinit var gameCountDownTimer: CountDownTimer // Initialized in 'onCreate()'

    override fun onCreate(savedInstanceState: Bundle?) {
        println("onCreate...")

        super.onCreate(savedInstanceState)

        foodDatabaseHelper = FoodDatabaseHelper(applicationContext)
        foodItemList = getFoodItemList(foodDatabaseHelper)

        // Grab values from content view
        setContentView(R.layout.activity_game)
        returnButton = findViewById(
            R.id.gameReturnFloatingActionButton
        )
        timerTextView = findViewById(
            R.id.timerTextView
        )
        foodNameTextView = findViewById(
            R.id.foodNameTextView
        )
        calorieCountGuessTextView = findViewById(
            R.id.calorieCountGuessTextView
        )
        caloriePickerSeekBar = findViewById(
            R.id.caloriePickerSeekBar
        )
        xCountValueTextView = findViewById(
            R.id.xCountValueTextView
        )
        pointsValueTextView = findViewById(
            R.id.pointsValueTextView
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
        caloriePickerSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (caloriePickerState != CaloriePickerState.PICKED) {
                    calorieCountGuessTextView.text = progress.toString()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // do nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Only allow 1 pick per food - so only process guess if current state is 'PICKING'
                if (caloriePickerState == CaloriePickerState.PICKING) {
                    caloriePickerState = CaloriePickerState.PICKED

                    guessCaloriesAndAwardPointsOrXs(seekBar.progress)

                    // Advance to next game state
                    if (xCount >= NUMBER_XS_FOR_GAME_OVER) {
                        toGameOver("Too many X's: $xCount")
                    } else {
                        toNextFoodItem()
                    }
                }
            }

        })

        // Initialize the timer
        gameCountDownTimer = object : CountDownTimer(TIME_LIMIT_MILLISECONDS, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                timerTextView.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                timerTextView.text = "Time's Up!"
                timerState = CountDownTimerState.STOPPED
                toGameOver("Time's Up!")
            }
        }
    }

    override fun onStart() {
        println("onStart...")
        super.onStart()
    }

    override fun onResume() {
        println("onResume...")
        super.onResume()

        updateCurrentFoodItem(foodItemList.random())

        // TODO - show a dialog box here before starting the timer

        // Don't start the timer over if it's already going
        if (timerState != CountDownTimerState.STARTED) {
            gameCountDownTimer.start()
            timerState = CountDownTimerState.STARTED
        }
    }

    override fun onPause() {
        println("onPause...")
        super.onPause()

        // TODO figure out how to pause the timer here?
    }

    override fun onStop() {
        println("onStop...")
        super.onStop()
    }

    override fun onDestroy() {
        println("onDestroy...")
        super.onDestroy()
    }

    // Game state change functions

    private fun toGameOver(reason: String?) {
        Toast.makeText(
            applicationContext,
            "Game Over" + if (!reason.isNullOrEmpty()) " - $reason" else "",
            Toast.LENGTH_SHORT
        ).show()

        // TODO - show a pop up here instead

        caloriePickerState = CaloriePickerState.PICKED
    }

    private fun toNextFoodItem() {
        updateCurrentFoodItem(foodItemList.random())
        caloriePickerSeekBar.setProgress(0, true)

        // Now that next food is setup, allow user to pick again
        caloriePickerState = CaloriePickerState.PICKING
    }

    // Helper methods

    private fun updateCurrentFoodItem(foodItem: FoodItem) {
        currentFoodItem = foodItem
        foodNameTextView.text = foodItem.name
    }

    private fun getFoodItemList(foodDatabaseHelper: FoodDatabaseHelper): List<FoodItem> {
        // TODO - eventually call the actual database for this

        return arrayListOf(
            FoodItem(1, "Banana", 150),
            FoodItem(2, "Pineapple", 200),
            FoodItem(3, "8oz Steak", 600),
            FoodItem(4, "Extra Large Steak Burrito", 900)
        )
    }

    private fun guessCaloriesAndAwardPointsOrXs(guessedCalories: Int) {
        Toast.makeText(applicationContext, "Guessed: $guessedCalories", Toast.LENGTH_SHORT).show()

        // TODO - increment points or Xs
        val calorieDifference = abs(currentFoodItem.calories - guessedCalories)
        if (calorieDifference <= MAX_CALORIE_GUESS_DIFFERENCE) {
            updatePointCount(pointCount + (MAX_CALORIE_GUESS_DIFFERENCE - calorieDifference)) // Earn points based on how close
        } else {
            updateXCount(++xCount)
        }
    }

    private fun updateXCount(xCount: Int) {
        this.xCount = xCount
        xCountValueTextView.text = this.xCount.toString()
    }

    private fun updatePointCount(pointCount: Int) {
        this.pointCount = pointCount
        pointsValueTextView.text = this.pointCount.toString()
    }

}