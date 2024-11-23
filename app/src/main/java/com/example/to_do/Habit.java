// File: app/src/main/java/com/example/habittracker/Habit.java
package com.example.to_do;

public class Habit {
    private int id;
    private String name;
    private int status;
    private String date;
    private long reminderTime;

    // Constructors
    public Habit() {}

    public Habit(int id, String name, int status, String date, long reminderTime) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.date = date;
        this.reminderTime = reminderTime;
    }

    // Getters and Setters

    // ID
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    // Name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // Status
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    // Date
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    // Reminder Time
    public long getReminderTime() {
        return reminderTime;
    }
    public void setReminderTime(long reminderTime) {
        this.reminderTime = reminderTime;
    }
}
