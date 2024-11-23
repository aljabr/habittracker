// File: app/src/main/java/com/example/to_do/PendingHabitsFragment.java
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

public class PendingHabitsFragment extends Fragment {

    private HabitAdapter adapter;

    public PendingHabitsFragment() {
        // Required empty public constructor
    }

    public static PendingHabitsFragment newInstance() {
        return new PendingHabitsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_habits, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPendingHabits);

        // Initialize ViewModel
        HabitViewModel habitViewModel = new ViewModelProvider(requireActivity()).get(HabitViewModel.class);

        // Initialize Adapter with ViewModel
        adapter = new HabitAdapter(getContext(), null, habitViewModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Observe data changes
        habitViewModel.getPendingHabits().observe(getViewLifecycleOwner(), habits -> adapter.updateHabitList(habits));

        return view;
    }
}
