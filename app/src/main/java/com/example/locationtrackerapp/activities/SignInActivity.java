package com.example.locationtrackerapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.locationtrackerapp.R;
import com.example.locationtrackerapp.utils.FirebaseAuthUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignInActivity extends AppCompatActivity {
    private TextInputLayout nameTextLayout;
    private TextInputLayout emailTextLayout;
    private TextInputLayout passwordTextLayout;

    private TextInputEditText nameTextInput;
    private TextInputEditText emailTextInput;
    private TextInputEditText passwordTextInput;

    MaterialButton logInButton;
    MaterialButton signInButton;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initUIComponents();
        loadListeners();
    }


    private void initUIComponents() {
        toolbar = findViewById(R.id.toolbar);

        nameTextLayout = findViewById(R.id.nameTextLayout);
        emailTextLayout = findViewById(R.id.emailTextLayout);
        passwordTextLayout = findViewById(R.id.passwordTextLayout);

        nameTextInput = findViewById(R.id.name);
        emailTextInput = findViewById(R.id.email);
        passwordTextInput = findViewById(R.id.password);

        logInButton = findViewById(R.id.logInButton);
        signInButton = findViewById(R.id.signInButton);
    }

    private void loadListeners() {
        signInButton.setOnClickListener(v -> signIn());

        logInButton.setOnClickListener(v -> redirectToLogIn());
    }

    private void redirectToLogIn() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    private void signIn() {
        String name = nameTextInput.getText().toString().trim();
        String email = emailTextInput.getText().toString().trim();
        String password = passwordTextInput.getText().toString().trim();

        // Validate the input fields
        if (TextUtils.isEmpty(name)) {
            nameTextLayout.setError("Please enter your name");
            return;
        } else {
            nameTextLayout.setErrorEnabled(false);
        }

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

        FirebaseAuthUtil firebaseAuthUtil = new FirebaseAuthUtil(this);
        firebaseAuthUtil.registerUser(email, password, name);

        // Clear the input fields
        nameTextInput.setText("");
        emailTextInput.setText("");
        passwordTextInput.setText("");
    }

}