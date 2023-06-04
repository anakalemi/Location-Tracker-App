package com.example.locationtrackerapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locationtrackerapp.R;
import com.example.locationtrackerapp.entities.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class UserMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;

    private ImageView toolbarBack;
    private User selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_map);

        toolbarBack = findViewById(R.id.toolbar_back);
        toolbarBack.setVisibility(View.VISIBLE);

        loadIntentExtras();
        loadListeners();

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void loadIntentExtras() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                if (extras.containsKey("user")) {
                    selectedUser = (User) extras.getSerializable("user");
                }
            }
        }
    }

    private void loadListeners() {
        toolbarBack.setOnClickListener(v -> finish());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng userLocation = new LatLng(selectedUser.getLatitude(), selectedUser.getLongitude());

        googleMap.addMarker(new MarkerOptions()
                        .position(userLocation)
                        .title(selectedUser.getName()))
                .showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f));
    }

}