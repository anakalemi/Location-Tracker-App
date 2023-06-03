package com.example.locationtrackerapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.locationtrackerapp.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class FirebaseUtils {

    private static final String TAG = "FirebaseUtils";

    private final FirebaseAuth mAuth;
    private Context mContext;

    public FirebaseUtils(Context context) {
        mContext = context;
        mAuth = FirebaseAuth.getInstance();
    }

    public void registerUser(String email, String password, final String name) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) mContext, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Log.d(TAG, "User profile updated.");
                                    }
                                });
                        Toast.makeText(mContext, "Registration successful.", Toast.LENGTH_SHORT).show();
                    } else {
                        String errorMessage = task.getException().getMessage();
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(mContext, "Registration failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) mContext, task -> {
                    if (task.isSuccessful()) {
                        // Login successful
                        Toast.makeText(mContext, "Login successful.", Toast.LENGTH_SHORT).show();

                        mContext.startActivity(new Intent(mContext, MainActivity.class));
                    } else {
                        String errorMessage = task.getException().getMessage();
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(mContext, "Login failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

}
