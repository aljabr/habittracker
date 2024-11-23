// File: app/src/main/java/com/example/habittracker/HabitAdapter.java
package com.example.to_do;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Date;
import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {
    private final Context context;
    private List<Habit> habitList;

    public HabitAdapter(Context context, List<Habit> habitList, HabitViewModel habitViewModel) {
        this.context = context;
        this.habitList = habitList;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.habit_item, parent, false);
        return new HabitViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit habit = habitList.get(position);
        holder.textViewName.setText(habit.getName());
        holder.textViewDate.setText("Added on: " + habit.getDate());

        // Display reminder time
        String reminderText = habit.getReminderTime() > 0 ? "Reminder: " + DateFormat.getTimeFormat(context).format(new Date(habit.getReminderTime())) : "No Reminder";
        holder.textViewReminderTime.setText(reminderText);

        // Check status to update UI
        if (habit.getStatus() == 1) {
            holder.buttonComplete.setText("Completed");
            holder.buttonComplete.setEnabled(false);
        } else {
            holder.buttonComplete.setText("Complete");
            holder.buttonComplete.setEnabled(true);
        }

        // Handle mark as completed
        holder.buttonComplete.setOnClickListener(v -> {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            dbHelper.updateHabitStatus(habit.getId(), 1);
            Toast.makeText(context, "Habit marked as completed!", Toast.LENGTH_SHORT).show();
            habit.setStatus(1);
            notifyItemChanged(position);

            // Cancel reminder
            cancelReminder(habit.getName());
        });

        // Handle delete habit
        holder.buttonDelete.setOnClickListener(v -> {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            dbHelper.deleteHabit(habit.getId());
            habitList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Habit deleted!", Toast.LENGTH_SHORT).show();

            // Cancel reminder
            cancelReminder(habit.getName());
        });
    }

    @Override
    public int getItemCount() {
        return habitList.size();
    }

    public void updateHabitList(List<Habit> newHabitList) {
        habitList = newHabitList;
        notifyDataSetChanged();
    }

    public static class HabitViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewDate, textViewReminderTime;
        Button buttonComplete, buttonDelete;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewReminderTime = itemView.findViewById(R.id.textViewReminderTime);
            buttonComplete = itemView.findViewById(R.id.buttonComplete);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }

    // Cancel Reminder
    private void cancelReminder(String habitName) {
        Intent intent = new Intent(context, ReminderBroadcastReceiver.class);
        intent.putExtra("habitName", habitName);
        int requestCode = habitName.hashCode();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
