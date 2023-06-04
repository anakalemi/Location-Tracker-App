package com.example.locationtrackerapp.services;

import android.location.Location;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        DatabaseReference userLocationRef = usersRef.child(userId).child("location");

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
    }

    public Location getUserLocation(String userId, OnLocationLoadedListener listener) {
        DatabaseReference userLocationRef = usersRef.child(userId).child("location");
        Location location = new Location("");

        userLocationRef.child("latitude").get()
                .addOnSuccessListener(latitudeSnapshot -> {
                    if (latitudeSnapshot.exists()) {
                        double latitude = latitudeSnapshot.getValue(Double.class);
                        location.setLatitude(latitude);

                        userLocationRef.child("longitude").get()
                                .addOnSuccessListener(longitudeSnapshot -> {
                                    if (longitudeSnapshot.exists()) {
                                        double longitude = longitudeSnapshot.getValue(Double.class);
                                        location.setLongitude(longitude);
                                        listener.onLocationLoaded(latitude, longitude);
                                    } else {
                                        Log.e(TAG, "Longitude does not exist for the user.");
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Failed to get longitude from Firebase: " + e.getMessage());
                                });
                    } else {
                        Log.e(TAG, "Latitude does not exist for the user.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get latitude from Firebase: " + e.getMessage());
                });

        return location;
    }

    public interface OnLocationLoadedListener {
        void onLocationLoaded(double latitude, double longitude);
    }

}
