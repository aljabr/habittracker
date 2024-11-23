// File: app/src/main/java/com/example/habittracker/DatabaseHelper.java
package com.example.to_do;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name and Version
    private static final String DATABASE_NAME = "HabitTracker.db";
    private static final int DATABASE_VERSION = 2; // Incremented version

    // Table Name
    public static final String TABLE_HABITS = "habits";

    // Column Names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_REMINDER_TIME = "reminder_time"; // New column

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HABITS_TABLE = "CREATE TABLE " + TABLE_HABITS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_STATUS + " INTEGER DEFAULT 0, "
                + COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + COLUMN_REMINDER_TIME + " INTEGER"
                + ")";
        db.execSQL(CREATE_HABITS_TABLE);
    }

    // Upgrading Database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add the new column for reminder time
            db.execSQL("ALTER TABLE " + TABLE_HABITS + " ADD COLUMN " + COLUMN_REMINDER_TIME + " INTEGER");
        }
    }

    // Insert New Habit
    public void addHabit(String name, long reminderTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_REMINDER_TIME, reminderTime);
        // Status and date have default values
        db.insert(TABLE_HABITS, null, values);
        db.close();
    }

    // Get All Habits
    public List<Habit> getAllHabits() {
        List<Habit> habitList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_HABITS + " ORDER BY " + COLUMN_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Habit habit = new Habit();
                habit.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                habit.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                habit.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                habit.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                habit.setReminderTime(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_REMINDER_TIME)));
                habitList.add(habit);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return habitList;
    }

    // Update Habit Status
    public void updateHabitStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);

        db.update(TABLE_HABITS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Delete Habit
    public void deleteHabit(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HABITS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Get Completed Habits
    public List<Habit> getCompletedHabits() {
        List<Habit> habitList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_HABITS + " WHERE " + COLUMN_STATUS + " = 1 ORDER BY " + COLUMN_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Habit habit = new Habit();
                habit.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                habit.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                habit.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                habit.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                habit.setReminderTime(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_REMINDER_TIME)));
                habitList.add(habit);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return habitList;
    }

    // Get Pending Habits
    public List<Habit> getPendingHabits() {
        List<Habit> habitList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_HABITS + " WHERE " + COLUMN_STATUS + " = 0 ORDER BY " + COLUMN_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Habit habit = new Habit();
                habit.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                habit.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                habit.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                habit.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                habit.setReminderTime(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_REMINDER_TIME)));
                habitList.add(habit);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return habitList;
    }

    // Get All Habits (Pending and Completed)
    public List<Habit> getAllHabitsAllStatuses() {
        List<Habit> habitList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_HABITS + " ORDER BY " + COLUMN_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Habit habit = new Habit();
                habit.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                habit.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                habit.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                habit.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                habit.setReminderTime(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_REMINDER_TIME)));
                habitList.add(habit);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return habitList;
    }

}
