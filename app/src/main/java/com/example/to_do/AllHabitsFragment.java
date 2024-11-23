package com.example.to_do;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AllHabitsFragment extends Fragment {

    private HabitAdapter adapter;

    // TextViews for stats
    private TextView textViewTotalHabits;
    private TextView textViewCompletedHabits;
    private TextView textViewPendingHabits;
    //private TextView textViewCompletionRate;

    public AllHabitsFragment() {
        // Required empty public constructor
    }

    public static AllHabitsFragment newInstance() {
        return new AllHabitsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_habits, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewAllHabits);

        // Initialize TextViews for stats
        textViewTotalHabits = view.findViewById(R.id.textViewTotalHabits);
        textViewCompletedHabits = view.findViewById(R.id.textViewCompletedHabits);
        textViewPendingHabits = view.findViewById(R.id.textViewPendingHabits);
//        textViewCompletionRate = view.findViewById(R.id.textViewCompletionRate);

        // Initialize ViewModel
        HabitViewModel habitViewModel = new ViewModelProvider(requireActivity()).get(HabitViewModel.class);

        // Initialize Adapter with ViewModel
        adapter = new HabitAdapter(getContext(), null, habitViewModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Observe data changes
        habitViewModel.getAllHabits().observe(getViewLifecycleOwner(), habits -> {
            adapter.updateHabitList(habits);
            updateStats(habits);
        });

        return view;
    }

    // Method to update stats
    private void updateStats(List<Habit> habits) {
        int totalHabits = habits.size();
        int completedHabits = 0;
        int pendingHabits = 0;

        for (Habit habit : habits) {
            if (habit.getStatus() == 1) {
                completedHabits++;
            } else {
                pendingHabits++;
            }
        }

//        double completionRate = totalHabits > 0 ? (completedHabits * 100.0 / totalHabits) : 0;

        textViewTotalHabits.setText(String.valueOf(totalHabits));
        textViewCompletedHabits.setText(String.valueOf(completedHabits));
        textViewPendingHabits.setText(String.valueOf(pendingHabits));
//        textViewCompletionRate.setText(String.format("%.2f%%", completionRate));
    }
}
