package com.example.locationtrackerapp.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locationtrackerapp.R;
import com.example.locationtrackerapp.utils.DrawerHelper;

public class MainActivity extends AppCompatActivity {
    private DrawerHelper drawerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerHelper = new DrawerHelper(this);
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