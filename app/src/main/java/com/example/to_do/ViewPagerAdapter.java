package com.example.to_do;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return AllHabitsFragment.newInstance();
            case 1:
                return PendingHabitsFragment.newInstance();
            case 2:
                return CompletedHabitsFragment.newInstance();
            default:
                return AllHabitsFragment.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Total number of tabs
    }
}
