// File: app/src/main/java/com/example/habittracker/HabitHistoryActivity.java
package com.example.to_do;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HabitHistoryActivity extends AppCompatActivity {

    private HabitAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Habit> completedHabits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_history);

        // Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_history);
        setSupportActionBar(toolbar);

        // Enable the Up button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back); // Use a back arrow icon
        }

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        RecyclerView recyclerViewHistory = findViewById(R.id.recyclerViewHistory);
        completedHabits = dbHelper.getCompletedHabits();
        adapter = new HabitAdapter(this, completedHabits);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHistory.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        completedHabits = dbHelper.getCompletedHabits();
        adapter.updateHabitList(completedHabits);
    }

    // Handle back button in toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Close the activity and return to previous
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
