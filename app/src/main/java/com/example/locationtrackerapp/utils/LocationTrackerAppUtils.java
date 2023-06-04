package com.example.locationtrackerapp.utils;

import com.example.locationtrackerapp.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LocationTrackerAppUtils {
    private static User currentUser = new User();

    public static String getCurrentUserName() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getDisplayName();
        }
        return "Current Location";
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        LocationTrackerAppUtils.currentUser = currentUser;
    }
}
