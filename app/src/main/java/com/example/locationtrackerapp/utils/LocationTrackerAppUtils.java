package com.example.locationtrackerapp.utils;

import android.app.Activity;
import android.content.Context;

import com.example.locationtrackerapp.R;
import com.example.locationtrackerapp.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import org.aviran.cookiebar2.CookieBar;

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

    public static void showCookieBarNoInternet(Context context) {
        CookieBar.build((Activity) context)
                .setIcon(R.drawable.ic_launcher_app_round)
                .setTitle("No Internet Connection")
                .setMessage("The device is not connected to the internet.")
                .setEnableAutoDismiss(true)
                .setSwipeToDismiss(true)
                .setCookiePosition(CookieBar.BOTTOM)
                .setBackgroundColor(R.color.app)
                .show();
    }
}
