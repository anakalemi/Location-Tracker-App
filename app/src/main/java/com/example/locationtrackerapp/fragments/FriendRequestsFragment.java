package com.example.locationtrackerapp.fragments;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationtrackerapp.R;
import com.example.locationtrackerapp.adapters.UserListAdapter;
import com.example.locationtrackerapp.entities.User;
import com.example.locationtrackerapp.entities.UserFriendRequest;
import com.example.locationtrackerapp.services.FirebaseUserService;
import com.example.locationtrackerapp.utils.LocationTrackerAppUtils;
import com.example.locationtrackerapp.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestsFragment extends Fragment {

    private View view;
    private final UserListAdapter adapter = new UserListAdapter(new ArrayList<>());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friend_requests, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.friendsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
        loadRecyclerViewAdapter();

        loadListeners();

        return view;
    }

    private void loadListeners() {
        adapter.setOnItemClickListener(position -> {
            User selectedUser = adapter.getUserList().get(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Friend Request")
                    .setMessage("Do you want to accept the friend request?")
                    .setPositiveButton("Accept", (dialogInterface, i) -> {
                        acceptFriendRequest(selectedUser);
                        loadRecyclerViewAdapter();
                    })
                    .setNegativeButton("Reject", (dialogInterface, i) -> {
                        rejectFriendRequest(selectedUser);
                        loadRecyclerViewAdapter();
                    })
                    .show();
        });
    }

    private void acceptFriendRequest(User selectedUser) {
        User currentUser = LocationTrackerAppUtils.getCurrentUser();
        currentUser.getFriendRequests().entrySet().stream()
                .filter(r -> r.getValue().getSenderUUID().equals(selectedUser.getUuid()))
                .findFirst().ifPresent(friendRequestEntry -> {
                    String requestUUID = friendRequestEntry.getKey();
                    if (NetworkUtils.isNetworkConnected(view.getContext())) {
                        new FirebaseUserService(view.getContext()).acceptFriendRequest(requestUUID, selectedUser.getUuid());
                    } else {
                        LocationTrackerAppUtils.showCookieBarNoInternet(view.getContext());
                    }
                });
    }

    private void rejectFriendRequest(User selectedUser) {
        User currentUser = LocationTrackerAppUtils.getCurrentUser();
        currentUser.getFriendRequests().entrySet().stream()
                .filter(r -> r.getValue().getSenderUUID().equals(selectedUser.getUuid()))
                .findFirst().ifPresent(friendRequestEntry -> {
                    String requestUUID = friendRequestEntry.getKey();
                    if (NetworkUtils.isNetworkConnected(view.getContext())) {
                        new FirebaseUserService(view.getContext()).rejectFriendRequest(requestUUID);
                    } else {
                        LocationTrackerAppUtils.showCookieBarNoInternet(view.getContext());
                    }
                });

    }

    private void loadRecyclerViewAdapter() {
        if (NetworkUtils.isNetworkConnected(view.getContext())) {
            new FirebaseUserService(view.getContext()).getAllUsers(new FirebaseUserService.UserCallback() {
                @Override
                public void onUsersLoaded(List<User> userList) {
                    List<User> usersOnRequest = new ArrayList<>();
                    User currentUser = LocationTrackerAppUtils.getCurrentUser();
                    for (UserFriendRequest request : currentUser.getFriendRequests().values()) {
                        for (User user : userList) {
                            if (request.getSenderUUID().equals(user.getUuid()) && request.getStatus() == UserFriendRequest.STATUS_PENDING) {
                                usersOnRequest.add(user);
                            }
                        }
                    }
                    adapter.setUserList(usersOnRequest);
                }

                @Override
                public void onDataCancelled(String errorMessage) {
                    Log.e(TAG, "Error retrieving users: " + errorMessage);
                }
            });
        } else {
            LocationTrackerAppUtils.showCookieBarNoInternet(view.getContext());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecyclerViewAdapter();
    }
}