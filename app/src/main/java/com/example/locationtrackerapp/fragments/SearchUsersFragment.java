package com.example.locationtrackerapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationtrackerapp.R;
import com.example.locationtrackerapp.adapters.UserListAdapter;
import com.example.locationtrackerapp.entities.User;

import java.util.ArrayList;
import java.util.List;

public class SearchUsersFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private UserListAdapter adapter;
    private List<User> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_users, container, false);

        recyclerView = view.findViewById(R.id.friendsRecyclerView);
        setRecyclerViewAdapter();
        loadListeners();
        return view;
    }

    private void loadListeners() {
        adapter.setOnItemClickListener(position -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Send Friend Request")
                    .setMessage("Do you want to send a friend request to this user?")
                    .setPositiveButton("Send", (dialogInterface, i) -> {
                        // todo sendFriendRequest();
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    }).show();
        });
    }

    private void setRecyclerViewAdapter() {
        List<User> userList = getUsers();
        adapter = new UserListAdapter(userList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
    }

    private List<User> getUsers() { //todo
        List<User> users = new ArrayList<>();
        users.add(new User("User", "user@gmail.com"));
        return users;
    }
}