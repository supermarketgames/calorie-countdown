package com.supermarketgames.caloriecountdown

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception
import android.util.Log

class FoodDatabaseHelper(
    context: Context?
) : SQLiteOpenHelper(context, DATABASE_NAME, DATABASE_FACTORY, DATABASE_VERSION) {
    // Companion object for static values
    companion object {
        // Database params
        const val DATABASE_NAME: String = "FoodDatabase.db"
        val DATABASE_FACTORY: SQLiteDatabase.CursorFactory? = null
        const val DATABASE_VERSION: Int = 1

        // Table params
        private const val TABLE_NAME = "food"
        private const val ID_COL = "id"
        private const val FOOD_ID_COL = "food_id"
        private const val FOOD_NAME_COL = "food_name"
        private const val FOOD_CALORIES_COL = "food_calories"

        // Base table queries
        private const val SQL_CREATE_ENTRIES = (
                "CREATE TABLE "
                        + TABLE_NAME
                        + " ("
                        + ID_COL
                        + " INTEGER PRIMARY KEY,"
                        + FOOD_ID_COL
                        + " TEXT, "
                        + FOOD_NAME_COL
                        + " TEXT, "
                        + FOOD_CALORIES_COL
                        + " DOUBLE)"
                )

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    // OnCreate
    override fun onCreate(db: SQLiteDatabase) {
        Log.d(
            "FoodDatabaseHelper",
            "onCreate: $DATABASE_NAME: $TABLE_NAME"
        )
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    // OnUpgrade
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        Log.d(
            "FoodDatabaseHelper",
            "onUpgrade: $DATABASE_NAME: $TABLE_NAME"
        )
        onCreate(db)
    }

    // Make sure table exists
    private fun createTable() {
        val db = this.writableDatabase
        val sqlCreateTable = (
            "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " ("
            + ID_COL
            + " INTEGER PRIMARY KEY,"
            + FOOD_ID_COL
            + " TEXT, "
            + FOOD_NAME_COL
            + " TEXT, "
            + FOOD_CALORIES_COL
            + " DOUBLE)"
        )
        db.execSQL(sqlCreateTable)
        Log.d(
            "FoodDatabaseHelper",
            "CreateTable: $DATABASE_NAME: $TABLE_NAME"
        )
    }

    // Check if item already exists
    private fun foodIdExists(foodId: Int?): Int {
        Log.d(
            "FoodDatabaseHelper",
            "ItemIdExists: $foodId $DATABASE_NAME: $TABLE_NAME"
        )
        var exists = 1
        val colName = "DoesUserExist"
        val db = this.writableDatabase
        val sqlFoodIdExists = (
                "SELECT CASE WHEN EXISTS(SELECT 1 FROM "
                        + TABLE_NAME
                        + " WHERE "
                        + FOOD_ID_COL
                        + " = \""
                        + foodId
                        + "\") THEN 1 ELSE 0 END AS "
                        + colName
                )

        // Run query
        try {
            db.rawQuery(sqlFoodIdExists, null).use { c ->
                c.moveToFirst()
                exists = c.getInt(c.getColumnIndex(colName))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.d(
            "FoodDatabaseHelper",
            "ItemIdExists: $foodId $DATABASE_NAME: $TABLE_NAME results: $exists"
        )
        return exists
    }

    // SetRow
    fun setRow(
        foodId: Int?,
        foodName: String?,
        foodCalories: Double?
    ): Int {
        if (foodIdExists(foodId) == 0) {
            Log.d(
                "FoodDatabaseHelper",
                "SetRow: $DATABASE_NAME: $TABLE_NAME"
            )
            createTable()
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(FOOD_ID_COL, foodId)
            contentValues.put(FOOD_NAME_COL, foodName)
            contentValues.put(FOOD_CALORIES_COL, foodCalories)
            db.insert(TABLE_NAME, null, contentValues)

            return 0
        } else {
            return -1
        }
    }

    // Remove row
    fun removeRow(foodId: Int): Int? {
        Log.d(
            "FoodDatabaseHelper",
            "removeRow: $foodId"
        )
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "food_id = ?", arrayOf(foodId.toString()))
    }

    // Return all data from database
    fun getData(): Cursor? {
        createTable()
        val db = this.writableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        return db.rawQuery(query, null)
    }

    // Return calories of specific food
    fun getCalories(foodId: Int): Double? {
        var output = -1.0
        if (foodIdExists(foodId) == 1) {
            Log.d(
                "FoodDatabaseHelper",
                "getCalories: $foodId"
            )
            createTable()
            val db = this.writableDatabase
            val query = """
                SELECT $FOOD_CALORIES_COL
                FROM $TABLE_NAME
                WHERE $FOOD_ID_COL == $foodId
                LIMIT 1
            """.trimIndent()

            val rawOutput = db.rawQuery(query, null)
            var stat = rawOutput.moveToFirst()
            while (stat) {
                output = rawOutput.getDouble(rawOutput.getColumnIndex(FOOD_CALORIES_COL))
                Log.d(
                    "FoodDatabaseHelper",
                    "getCalories: $foodId stat: $stat"
                )
                stat = rawOutput.moveToNext()
            }
            rawOutput.close()
        }
        return output
    }

}