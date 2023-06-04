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

import java.util.ArrayList;
import java.util.List;

public class SearchUsersFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private final UserListAdapter adapter = new UserListAdapter(new ArrayList<>());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_users, container, false);

        recyclerView = view.findViewById(R.id.friendsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        loadRecyclerViewAdapter();
        loadListeners();
        return view;
    }

    private void loadListeners() {
        adapter.setOnItemClickListener(position -> {
            User selectedUser = adapter.getUserList().get(position);
            User currentUser = LocationTrackerAppUtils.getCurrentUser();
            boolean isSentRequestPending = selectedUser.getFriendRequests().values().stream()
                    .anyMatch(r -> r.getSenderUUID().equals(currentUser.getUuid())
                            && r.getStatus() == UserFriendRequest.STATUS_PENDING);

            boolean isSentRequestAccepted = selectedUser.getFriendRequests().values().stream()
                    .anyMatch(r -> r.getSenderUUID().equals(currentUser.getUuid())
                            && r.getStatus() == UserFriendRequest.STATUS_ACCEPTED);

            boolean isRequestPending = currentUser.getFriendRequests().values().stream()
                    .anyMatch(r -> r.getSenderUUID().equals(selectedUser.getUuid())
                            && r.getStatus() == UserFriendRequest.STATUS_PENDING);

            boolean isRequestAccepted = currentUser.getFriendRequests().values().stream()
                    .anyMatch(r -> r.getSenderUUID().equals(selectedUser.getUuid())
                            && r.getStatus() == UserFriendRequest.STATUS_ACCEPTED);

            if (isSentRequestPending) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Friend Request Pending")
                        .setMessage("Wait for " + selectedUser.getName() + " to respond.")
                        .setNegativeButton("Ok", (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
            } else if (isRequestPending) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Friend Request Pending")
                        .setMessage("Go to Requests to respond.")
                        .setNegativeButton("Ok", (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
            } else if (isRequestAccepted || isSentRequestAccepted) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Friend Request")
                        .setMessage(selectedUser.getName() + " is already in your friend list.")
                        .setNegativeButton("Ok", (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Send Friend Request")
                        .setMessage("Do you want to send a friend request to " + selectedUser.getName() + "?")
                        .setPositiveButton("Send", (dialogInterface, i) -> sendFriendRequest(selectedUser))
                        .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
            }
        });
    }

    private void sendFriendRequest(User selectedUser) {
        new FirebaseUserService(view.getContext()).sendFriendRequest(selectedUser.getUuid());
    }

    private void loadRecyclerViewAdapter() {
        new FirebaseUserService(view.getContext()).getAllUsers(new FirebaseUserService.UserCallback() {
            @Override
            public void onUsersLoaded(List<User> userList) {
                adapter.setUserList(userList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onDataCancelled(String errorMessage) {
                Log.e(TAG, "Error retrieving users: " + errorMessage);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecyclerViewAdapter();
    }

}