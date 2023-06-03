package com.example.locationtrackerapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.locationtrackerapp.R;
import com.example.locationtrackerapp.utils.FirebaseUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LogInActivity extends AppCompatActivity {

    private TextInputLayout emailTextLayout;
    private TextInputLayout passwordTextLayout;

    private TextInputEditText emailTextInput;
    private TextInputEditText passwordTextInput;

    MaterialButton logInButton;
    MaterialButton signInButton;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        initUIComponents();
        loadListeners();
    }

    private void initUIComponents() {
        toolbar = findViewById(R.id.toolbar);

        emailTextLayout = findViewById(R.id.emailTextLayout);
        passwordTextLayout = findViewById(R.id.passwordTextLayout);

        emailTextInput = findViewById(R.id.email);
        passwordTextInput = findViewById(R.id.password);

        logInButton = findViewById(R.id.logInButton);
        signInButton = findViewById(R.id.signInButton);
    }

    private void loadListeners() {
        logInButton.setOnClickListener(v -> logIn());

        signInButton.setOnClickListener(v -> redirectToSignIn());
    }

    private void redirectToSignIn() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
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

        FirebaseUtils firebaseUtils = new FirebaseUtils(this);
        firebaseUtils.loginUser(email, password);

        // Clear the input fields
        emailTextInput.setText("");
        passwordTextInput.setText("");
    }

}