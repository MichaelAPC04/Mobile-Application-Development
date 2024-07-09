package com.example.a1;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Objects;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    EditText editTextUsername, editTextPassword, editTextConfirmPassword;
    String checkUsername, checkPassword, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

    }

    public void onRegisterButton(View view){

        checkUsername = editTextUsername.getText().toString();
        checkPassword = editTextPassword.getText().toString();
        confirmPassword = editTextConfirmPassword.getText().toString();

        if (checkUsername.matches("")){
            String message = "Username field cannot be empty.";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        else if (checkPassword.matches("") || (confirmPassword.matches(""))){
            String message = "Password field/s cannot be empty.";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        else if (!Objects.equals(checkPassword, confirmPassword)){
            String message = "Passwords do not match, please try again";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        else if (!Objects.equals(checkUsername, "") && !Objects.equals(checkPassword, "") &&
                !Objects.equals(confirmPassword, "") && checkPassword.matches(confirmPassword)){

            // Save username & password if they meet conditions above, then move to login window.
            saveDataToSharedPreferences(checkUsername, checkPassword, confirmPassword);

            String message = "Registration successful";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            Intent loginIntent = new Intent(this, LoginWindow.class);
            startActivity(loginIntent);

        }

        else {
            String message = "Registered failed, please try again, ensure no fields are blank";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

    }

    public void onLoginButton(View view) {

        Intent loginIntent = new Intent(this, LoginWindow.class);
        startActivity(loginIntent);   // If already registered, move straight to the login window.

    }

    // Save user data to shared preferences.
    private void saveDataToSharedPreferences(String checkUsername, String checkPassword,
                                             String confirmPassword){
        SharedPreferences sP = getSharedPreferences(KeyStore.SP_FILENAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.putString(KeyStore.USERNAME_KEY, checkUsername);
        editor.putString(KeyStore.PW_KEY, checkPassword);
        editor.putString(KeyStore.CONFIRM_PW_KEY, confirmPassword);
        editor.apply();

    }

}