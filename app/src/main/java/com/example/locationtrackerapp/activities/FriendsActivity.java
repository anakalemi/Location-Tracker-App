package com.example.locationtrackerapp.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationtrackerapp.R;
import com.example.locationtrackerapp.adapters.FriendsListAdapter;
import com.example.locationtrackerapp.entities.User;
import com.example.locationtrackerapp.entities.UserFriend;
import com.example.locationtrackerapp.services.FirebaseUserService;
import com.example.locationtrackerapp.utils.DrawerHelper;
import com.example.locationtrackerapp.utils.LocationTrackerAppUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    private DrawerHelper drawerHelper;
    private final FriendsListAdapter adapter = new FriendsListAdapter(new ArrayList<>());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        drawerHelper = new DrawerHelper(this);

        RecyclerView recyclerView = findViewById(R.id.friendsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        loadRecyclerViewAdapter();

        loadListeners();
    }

    private void loadListeners() {
        adapter.setOnItemClickListener(position -> {
            User selectedUser = adapter.getUserList().get(position);
            Intent intent = new Intent(this, UserMapActivity.class);
            intent.putExtra("user", selectedUser);
            startActivity(intent);
        });

        FloatingActionButton manageRequestsFAB = findViewById(R.id.manageRequestsFAB);
        manageRequestsFAB.setOnClickListener(v -> {
            Intent intent = new Intent(FriendsActivity.this, FriendsManagerActivity.class);
            startActivity(intent);
        });
    }

    private void loadRecyclerViewAdapter() {
        new FirebaseUserService(this).getAllUsers(new FirebaseUserService.UserCallback() {
            @Override
            public void onUsersLoaded(List<User> userList) {
                List<User> friendsList = new ArrayList<>();
                for (User user : userList) {
                    UserFriend userFriend = user.getFriends().values().stream()
                            .filter(f -> f.getUuid().equals(LocationTrackerAppUtils.getCurrentUser().getUuid()))
                            .findAny()
                            .orElse(null);
                    if (userFriend != null && userFriend.getStatus() == UserFriend.STATUS_CONNECTED) {
                        friendsList.add(user);
                    }
                }

                adapter.setUserList(friendsList);
            }

            @Override
            public void onDataCancelled(String errorMessage) {
                Log.e(TAG, "Error retrieving users: " + errorMessage);
            }
        });
    }

    @Override
    public void onBackPressed() {
        drawerHelper.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerHelper.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecyclerViewAdapter();
    }

}