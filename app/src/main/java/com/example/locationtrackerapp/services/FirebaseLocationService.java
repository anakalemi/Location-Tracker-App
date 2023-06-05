package com.example.locationtrackerapp.services;

import android.location.Location;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FirebaseLocationService {

    private static final String TAG = "FirebaseLocationService";
    private final DatabaseReference usersRef;

    public FirebaseLocationService() {
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
    }

    public void storeCurrentUserLocation(double latitude, double longitude) {
        storeLocationInFirebase(latitude, longitude);
    }

    private void storeLocationInFirebase(double latitude, double longitude) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userLocationRef = usersRef.child(userId);

        userLocationRef.child("latitude").setValue(latitude)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Latitude stored in Firebase.");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error storing latitude in Firebase: " + e.getMessage());
                });

        userLocationRef.child("longitude").setValue(longitude)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Longitude stored in Firebase.");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error storing longitude in Firebase: " + e.getMessage());
                });

        String lastUpdate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        userLocationRef.child("lastUpdate").setValue(lastUpdate)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DateTime stored in Firebase.");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error storing DateTime in Firebase: " + e.getMessage());
                });
    }

}
