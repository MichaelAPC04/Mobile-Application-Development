package com.example.a1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class LoginWindow extends AppCompatActivity {

    EditText editTextLoginUsername, editTextLoginPassword;
    String validatePassword, validateUsername, usernameRestored, passwordRestored;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_window);

        editTextLoginUsername = findViewById(R.id.editTextLoginUsername);
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword);

        // Get the username and password that the user previously registered with.
        SharedPreferences sP = getSharedPreferences(KeyStore.SP_FILENAME, MODE_PRIVATE);
        usernameRestored = sP.getString(KeyStore.USERNAME_KEY, "");
        passwordRestored = sP.getString(KeyStore.PW_KEY, "");

        editTextLoginUsername.setText(usernameRestored);   // Pre-fill the username field

    }

    public void registerButton(View view){
        Intent registerIntent = new Intent(this, MainActivity.class);
        startActivity(registerIntent);   // Move back to the registration window.
    }

    public void loginButton(View view){
        validatePassword = editTextLoginPassword.getText().toString();
        validateUsername = editTextLoginUsername.getText().toString();

        // Usernames must match if changed from the pre-fill.
        if (!Objects.equals(usernameRestored, validateUsername)){
            String message = "Invalid username input";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        else if (validatePassword.matches("")){
            String message = "You must input your password";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        else if (validatePassword.matches(passwordRestored)){
            String message = "Logged in successfully";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Intent dashboardIntent = new Intent(this, DashboardWindow.class);
            startActivity(dashboardIntent);   // Password is correct, move to the dashboard.
        }

        else {
            String message = "Invalid password, please try again or register";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

    }

}