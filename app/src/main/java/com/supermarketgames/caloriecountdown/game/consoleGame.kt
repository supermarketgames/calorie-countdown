package com.supermarketgames.caloriecountdown.game

import java.time.Instant
import kotlin.random.Random

fun main(args: Array<String>) {
    println("Game Start")

    val randomNumberGenerator = Random(1)

    // Constant game values
    val timeLimitSeconds = 60
    val numberXsForGameOver = 3
    val startTime = Instant.now()

    // User's score and X's
    var pointCount = 0
    var numberXsReceived = 0

    // The Game Loop
    while (true) {
        // Display how much time is left
        println(
            "Time Left: ${
                timeLimitSeconds - ((Instant.now().toEpochMilli() - startTime.toEpochMilli()) / 1000)
            }"
        )

        // Get next calorie number to guess
        val calorieNumber = randomNumberGenerator.nextInt(0, 3)

        // Ask the user to guess
        println("Guess the number of calories between 0 and 3 (inclusive):")

        // Check user's input
        val answerNumber = readLine()
        if (answerNumber != null && answerNumber.toInt() == calorieNumber) {
            println("Yep! You guessed it!")
            pointCount++
        } else {
            println("Nope - the number was actually $calorieNumber")
            numberXsReceived++
        }

        // Update Score and 'X's
        println("Score: $pointCount")
        print("X's  : ")
        for(i in 0 until numberXsForGameOver) {
            print("[${if(i < numberXsReceived) "X" else " "}]")
        }
        println()

        // If Timer is at 0 or less --> Game Over
        if (timeLimitSeconds - ((Instant.now().toEpochMilli() - startTime.toEpochMilli()) / 1000) <= 0) {
            println("Time's up!")
            break;
        }

        // If number of 'X's reached --> Game Over
        if (numberXsReceived >= numberXsForGameOver) {
            println("That's $numberXsForGameOver X's!")
            break;
        }

        // If User wants to quit --> Game Over
        println("Ready to quit (y/n)?")
        val answerQuit = readLine()
        if (answerQuit != null && answerQuit.matches(Regex("^[yY]"))) {
            println("Quitting game...")
            break;
        }
    }

    println("Game End")
}

