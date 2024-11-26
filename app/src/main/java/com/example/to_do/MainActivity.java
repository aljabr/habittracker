package com.example.to_do;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import android.widget.EditText;
import android.widget.Button;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private HabitViewModel habitViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        // Request Notification Permission
        requestNotificationPermission();
        // Initialize ViewPager and TabLayout
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        // Attach TabLayout and ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("All");
                            break;
                        case 1:
                            tab.setText("Pending");
                            break;
                        case 2:
                            tab.setText("Completed");
                            break;
                    }
                }).attach();

        // Initialize ViewModel
        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);

        // Floating Action Button to add new habit
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> showAddHabitBottomSheet());
    }



    // Show the bottom sheet dialog for adding a habit
    private void showAddHabitBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_add_habit, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        EditText editTextHabitName = bottomSheetView.findViewById(R.id.editTextHabitName);
        Button buttonSave = bottomSheetView.findViewById(R.id.buttonSave);
        Button buttonCancel = bottomSheetView.findViewById(R.id.buttonCancel);
        Button buttonSetReminder = bottomSheetView.findViewById(R.id.buttonSetReminder);
        TextView textViewReminderTime = bottomSheetView.findViewById(R.id.textViewReminderTime);

        final long[] reminderTime = {0}; // To store selected time

        buttonSetReminder.setOnClickListener(v -> showTimePickerDialog(textViewReminderTime, reminderTime));

        buttonSave.setOnClickListener(v -> {
            String habitName = editTextHabitName.getText().toString().trim();
            if (!habitName.isEmpty()) {
                DatabaseHelper dbHelper = new DatabaseHelper(this);
                dbHelper.addHabit(habitName, reminderTime[0]);
                Toast.makeText(MainActivity.this, "Habit added successfully!", Toast.LENGTH_SHORT).show();

                // Refresh data in ViewModel
                habitViewModel.refreshData();

                bottomSheetDialog.dismiss();

                if (reminderTime[0] > 0) {
                    scheduleReminder(habitName, reminderTime[0]);
                }
            } else {
                Toast.makeText(MainActivity.this, "Please enter a habit name.", Toast.LENGTH_SHORT).show();
            }
        });

        buttonCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }

    // Show TimePickerDialog to set reminder time
    private void showTimePickerDialog(TextView textView, long[] reminderTime) {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
            Calendar selectedTime = Calendar.getInstance();
            selectedTime.set(Calendar.HOUR_OF_DAY, selectedHour);
            selectedTime.set(Calendar.MINUTE, selectedMinute);
            selectedTime.set(Calendar.SECOND, 0);
            selectedTime.set(Calendar.MILLISECOND, 0);

            // If the selected time is before the current time, schedule for the next day
            if (selectedTime.before(currentTime)) {
                selectedTime.add(Calendar.DAY_OF_MONTH, 1);
            }

            reminderTime[0] = selectedTime.getTimeInMillis();
            String timeText = "Reminder set for: " + DateFormat.getTimeFormat(this).format(new Date(reminderTime[0]));
            textView.setText(timeText);

            // Add the toast message confirming the reminder is set
            Toast.makeText(MainActivity.this, "Reminder successfully set!", Toast.LENGTH_SHORT).show(); // Fifth Toast Message
        }, hour, minute, true);

        timePickerDialog.show();
    }


    // Schedule Reminder using AlarmManager
    private void scheduleReminder(String habitName, long reminderTimeInMillis) {
        Intent intent = new Intent(this, ReminderBroadcastReceiver.class);
        intent.putExtra("habitName", habitName);

        int requestCode = habitName.hashCode(); // Unique request code
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            // For repeating alarm
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, reminderTimeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    // Request Notification Permission for Android 13 and above
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1001);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
