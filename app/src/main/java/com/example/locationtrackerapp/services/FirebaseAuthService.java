package com.example.locationtrackerapp.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.locationtrackerapp.activities.MainActivity;
import com.example.locationtrackerapp.utils.SessionManager;
import com.example.locationtrackerapp.utils.SharedPreferencesUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class FirebaseAuthService {

    private static final String TAG = "FirebaseAuthService";

    private final FirebaseAuth mAuth;
    private final Context mContext;

    public FirebaseAuthService(Context context) {
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
                                        new FirebaseUserService(mContext).storeUserInFirebase();
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
                        Log.d(TAG, "Login successful.");
                        storeCredentialsInSharedPreferences(email, password);
                        saveUserSession(email);
                        new FirebaseUserService(mContext)
                                .loadCurrentUserByUuid(FirebaseAuth.getInstance().getUid());

                        mContext.startActivity(new Intent(mContext, MainActivity.class));
                        ((Activity) mContext).finish();
                    } else {
                        String errorMessage = task.getException().getMessage();
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(mContext, "Login failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserSession(String email) {
        SessionManager sessionManager = new SessionManager(mContext);
        sessionManager.saveSession(mAuth.getCurrentUser().getUid(), email);
    }

    private void storeCredentialsInSharedPreferences(String email, String password) {
        SharedPreferencesUtils sharedPreferences = new SharedPreferencesUtils(mContext);
        sharedPreferences.saveString(SharedPreferencesUtils.EMAIL_KEY, email);
        sharedPreferences.saveString(SharedPreferencesUtils.PASS_KEY, password);
    }

}
