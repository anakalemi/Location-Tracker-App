package com.example.locationtrackerapp.activities;

import android.os.Bundle;
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
import com.example.locationtrackerapp.utils.FirebaseAuthUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private TextView loggedInName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configDrawerMenu();
    }

    private void configDrawerMenu() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_24);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        loggedInName = navigationView.getHeaderView(0).findViewById(R.id.loggedInName);
        setCurrentUserNameInNavHeader();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setCurrentUserNameInNavHeader() {
        FirebaseUser currentUser = new FirebaseAuthUtil(this).getmAuth().getCurrentUser();
        if (currentUser != null) {
            loggedInName.setText(currentUser.getDisplayName());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profileItem) {
            //Todo
        } else if (item.getItemId() == R.id.friendsItem) {
            //Todo
        } else if (item.getItemId() == R.id.logOutItem) {
            // Todo
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        // Close the drawer when the back button is pressed
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}