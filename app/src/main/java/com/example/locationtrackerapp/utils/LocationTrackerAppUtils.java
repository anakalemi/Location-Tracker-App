package com.example.locationtrackerapp.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LocationTrackerAppUtils {

    public static String getCurrentUserName() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getDisplayName();
        }
        return "Current Location";
    }
}
