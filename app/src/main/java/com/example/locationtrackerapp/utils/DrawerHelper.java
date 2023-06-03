package com.example.locationtrackerapp.utils;

import android.content.Intent;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.locationtrackerapp.R;
import com.example.locationtrackerapp.activities.LogInActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;

public class DrawerHelper implements NavigationView.OnNavigationItemSelectedListener {
    private AppCompatActivity activity;
    private DrawerLayout drawerLayout;
    private TextView loggedInName;

    public DrawerHelper(AppCompatActivity activity) {
        this.activity = activity;
        configDrawerMenu();
    }

    private void configDrawerMenu() {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_24);
        }

        drawerLayout = activity.findViewById(R.id.drawer_layout);
        NavigationView navigationView = activity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        loggedInName = navigationView.getHeaderView(0).findViewById(R.id.loggedInName);
        setCurrentUserNameInNavHeader();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setCurrentUserNameInNavHeader() {
        FirebaseUser currentUser = new FirebaseAuthUtil(activity).getmAuth().getCurrentUser();
        if (currentUser != null) {
            loggedInName.setText(currentUser.getDisplayName());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profileItem) {
            // Todo: Handle profile item click
        } else if (item.getItemId() == R.id.friendsItem) {
            // Todo: Handle friends item click
        } else if (item.getItemId() == R.id.logOutItem) {
            logout();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        SessionManager sessionManager = new SessionManager(activity);
        sessionManager.logout();
        Intent intent = new Intent(activity, LogInActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public void onBackPressed() {
        // Close the drawer when the back button is pressed
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            activity.onBackPressed();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }
}