package com.example.locationtrackerapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.locationtrackerapp.R;
import com.example.locationtrackerapp.adapters.FriendsListAdapter;
import com.example.locationtrackerapp.entities.User;
import com.example.locationtrackerapp.utils.DrawerHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    private FloatingActionButton manageRequestsFAB;
    private DrawerHelper drawerHelper;
    private RecyclerView recyclerView;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        drawerHelper = new DrawerHelper(this);

        recyclerView = findViewById(R.id.friendsRecyclerView);
        setRecyclerViewAdapter();

        manageRequestsFAB = findViewById(R.id.manageRequestsFAB);
        manageRequestsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsActivity.this, FriendsManagerActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setRecyclerViewAdapter() {
        List<User> userList = getUsers();
        FriendsListAdapter adapter = new FriendsListAdapter(userList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
    }

    private List<User> getUsers() { //todo
        List<User> users = new ArrayList<>();
        users.add(new User("Friend", "friend@gmail.com"));
        return users;
    }

    @Override
    public void onBackPressed() {
        drawerHelper.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerHelper.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}