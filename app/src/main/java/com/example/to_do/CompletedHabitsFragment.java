package com.example.to_do;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

public class CompletedHabitsFragment extends Fragment {

    private HabitAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Habit> habitList;

    public CompletedHabitsFragment() {
        // Required empty public constructor
    }

    public static CompletedHabitsFragment newInstance() {
        return new CompletedHabitsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_habits, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCompletedHabits);
        dbHelper = new DatabaseHelper(getContext());
        habitList = dbHelper.getCompletedHabits();
        adapter = new HabitAdapter(getContext(), habitList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        habitList = dbHelper.getCompletedHabits();
        adapter.updateHabitList(habitList);
    }
}
