package com.example.locationtrackerapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.locationtrackerapp.R;
import com.example.locationtrackerapp.services.FirebaseAuthService;
import com.example.locationtrackerapp.utils.LocationTrackerAppUtils;
import com.example.locationtrackerapp.utils.NetworkUtils;
import com.example.locationtrackerapp.utils.SessionManager;
import com.example.locationtrackerapp.utils.SharedPreferencesUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LogInActivity extends AppCompatActivity {

    private TextInputLayout emailTextLayout;
    private TextInputLayout passwordTextLayout;

    private TextInputEditText emailTextInput;
    private TextInputEditText passwordTextInput;

    private MaterialButton logInButton;
    private MaterialButton signInButton;
    private MaterialButton biometricIdentificationButton;
    private SharedPreferencesUtils sharedPreferencesUtils;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        initUIComponents();
        sessionCheck();
        loadListeners();
    }

    private void sessionCheck() {
        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initUIComponents() {
        toolbar = findViewById(R.id.toolbar);

        emailTextLayout = findViewById(R.id.emailTextLayout);
        passwordTextLayout = findViewById(R.id.passwordTextLayout);

        emailTextInput = findViewById(R.id.email);
        passwordTextInput = findViewById(R.id.password);

        logInButton = findViewById(R.id.logInButton);
        signInButton = findViewById(R.id.signInButton);
        biometricIdentificationButton = findViewById(R.id.biometricIdentificationButton);

        sharedPreferencesUtils = new SharedPreferencesUtils(this);
        if (sharedPreferencesUtils.getString(SharedPreferencesUtils.EMAIL_KEY).isEmpty()
                || sharedPreferencesUtils.getString(SharedPreferencesUtils.PASS_KEY).isEmpty()) {
            biometricIdentificationButton.setVisibility(View.GONE);
        } else {
            emailTextInput.setText(sharedPreferencesUtils.getString(SharedPreferencesUtils.EMAIL_KEY));
        }
    }

    private void loadListeners() {
        logInButton.setOnClickListener(v -> logIn());

        biometricIdentificationButton.setOnClickListener(v -> {
            if (checkBiometricSupport()) {
                authenticateUser(v);
            }
        });

        signInButton.setOnClickListener(v -> redirectToSignIn());
    }

    private void redirectToSignIn() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    private void logIn() {
        String email = emailTextInput.getText().toString().trim();
        String password = passwordTextInput.getText().toString().trim();

        // Validate the input fields
        if (TextUtils.isEmpty(email)) {
            emailTextLayout.setError("Please enter an email");
            return;
        } else {
            emailTextLayout.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(password)) {
            passwordTextLayout.setError("Please enter a password");
            return;
        } else {
            passwordTextLayout.setErrorEnabled(false);
        }

        if (NetworkUtils.isNetworkConnected(this)) {
            FirebaseAuthService firebaseAuthService = new FirebaseAuthService(this);
            firebaseAuthService.loginUser(email, password);
        } else {
            LocationTrackerAppUtils.showCookieBarNoInternet(this);
        }

        // Clear the input fields
        emailTextInput.setText("");
        passwordTextInput.setText("");
    }

    private void notifyUser(String message) {
        Toast.makeText(this,
                message,
                Toast.LENGTH_LONG).show();
    }

    private Boolean checkBiometricSupport() {
        PackageManager packageManager = this.getPackageManager();

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.USE_BIOMETRIC) !=
                PackageManager.PERMISSION_GRANTED) {

            notifyUser("Fingerprint authentication permission not enabled");
            return false;
        }

        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            notifyUser("Not a system feature");
            return false;
        }

        return true;
    }

    private BiometricPrompt.AuthenticationCallback getAuthenticationCallback() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            return new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode,
                                                  CharSequence errString) {
                    notifyUser("Authentication error: " + errString);
                    super.onAuthenticationError(errorCode, errString);
                }

                @Override
                public void onAuthenticationHelp(int helpCode,
                                                 CharSequence helpString) {
                    super.onAuthenticationHelp(helpCode, helpString);
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                }

                @Override
                public void onAuthenticationSucceeded(
                        BiometricPrompt.AuthenticationResult result) {
                    notifyUser("Authentication Succeeded");

                    String email = sharedPreferencesUtils.getString(SharedPreferencesUtils.EMAIL_KEY);
                    String password = sharedPreferencesUtils.getString(SharedPreferencesUtils.PASS_KEY);

                    if (NetworkUtils.isNetworkConnected(LogInActivity.this)) {
                        FirebaseAuthService firebaseAuthService = new FirebaseAuthService(LogInActivity.this);
                        firebaseAuthService.loginUser(email, password);
                    } else {
                        LocationTrackerAppUtils.showCookieBarNoInternet(LogInActivity.this);
                    }

                    super.onAuthenticationSucceeded(result);
                }
            };
        }
        return null;
    }

    private CancellationSignal getCancellationSignal() {
        CancellationSignal cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener((CancellationSignal.OnCancelListener)
                () -> notifyUser("Cancelled via signal"));
        return cancellationSignal;
    }

    public void authenticateUser(View view) {
        BiometricPrompt biometricPrompt = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            biometricPrompt = new BiometricPrompt.Builder(this)
                    .setTitle("Biometric Identification")
                    .setSubtitle("Authentication is required to continue")
                    .setNegativeButton("Cancel", this.getMainExecutor(),
                            (dialogInterface, i) -> notifyUser("Authentication cancelled"))
                    .build();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            biometricPrompt.authenticate(getCancellationSignal(), getMainExecutor(),
                    getAuthenticationCallback());
        }
    }

}