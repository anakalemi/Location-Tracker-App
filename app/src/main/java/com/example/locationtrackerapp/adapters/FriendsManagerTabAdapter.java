package com.example.locationtrackerapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.locationtrackerapp.fragments.FriendRequestsFragment;
import com.example.locationtrackerapp.fragments.SearchUsersFragment;

public class FriendsManagerTabAdapter extends FragmentStateAdapter {

    private final FriendRequestsFragment friendRequestsFragment = new FriendRequestsFragment();
    private final SearchUsersFragment searchUsersFragment = new SearchUsersFragment();

    public FriendsManagerTabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
            default:
                return searchUsersFragment;
            case 1:
                return friendRequestsFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
