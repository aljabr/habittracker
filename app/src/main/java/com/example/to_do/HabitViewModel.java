// File: app/src/main/java/com/example/to_do/HabitViewModel.java
package com.example.to_do;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class HabitViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Habit>> allHabits;
    private final MutableLiveData<List<Habit>> pendingHabits;
    private final MutableLiveData<List<Habit>> completedHabits;

    private final DatabaseHelper dbHelper;

    public HabitViewModel(@NonNull Application application) {
        super(application);
        dbHelper = new DatabaseHelper(application.getApplicationContext());
        allHabits = new MutableLiveData<>();
        pendingHabits = new MutableLiveData<>();
        completedHabits = new MutableLiveData<>();
        loadHabits();
    }

    private void loadHabits() {
        allHabits.setValue(dbHelper.getAllHabitsAllStatuses());
        pendingHabits.setValue(dbHelper.getPendingHabits());
        completedHabits.setValue(dbHelper.getCompletedHabits());
    }

    public LiveData<List<Habit>> getAllHabits() {
        return allHabits;
    }

    public LiveData<List<Habit>> getPendingHabits() {
        return pendingHabits;
    }

    public LiveData<List<Habit>> getCompletedHabits() {
        return completedHabits;
    }

    public void refreshData() {
        loadHabits();
    }
}
