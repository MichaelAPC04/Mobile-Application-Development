package com.example.a1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

import java.security.Key;
import java.util.Objects;
import java.util.StringTokenizer;

public class NewEventCategory extends AppCompatActivity {

    EditText EditTextCategoryID, EditTextCategoryName, EditTextEventCount;
    Switch SwitchIsActive;
    String CategoryID, CategoryName, EventCount;
    Boolean IsActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_category);

        EditTextCategoryID = findViewById(R.id.editTextCategoryID);
        EditTextCategoryName = findViewById(R.id.editTextCategoryName);
        EditTextEventCount = findViewById(R.id.editTextEventCount);
        SwitchIsActive = findViewById(R.id.switchIsActive);

        // Enabling SMS permissions.
        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.RECEIVE_SMS,
                android.Manifest.permission.READ_SMS
        }, 0);

        MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER), RECEIVER_EXPORTED);

    }

    public void onClickCategoryID(View view){

        CategoryID = generateCategoryID();
        CategoryName = EditTextCategoryName.getText().toString();
        EventCount = EditTextEventCount.getText().toString();
        IsActive = SwitchIsActive.isChecked();

        if (CategoryName.matches("")){
            String message = "The category name cannot be blank";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        else if (EventCount.matches("")){
            String message = "The event count cannot be blank";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        else if (!Objects.equals(CategoryName, "") && !Objects.equals(EventCount, "") &&
                IsActive.equals(true) || IsActive.equals(false)){
            // Save new event category data to shared preferences.
            saveEventCategoryDataToSharedPreference(CategoryID, CategoryName, EventCount, IsActive);
            EditTextCategoryID.setText(CategoryID); // Pre-fill with the auto-generated code.
            String message = "Category saved successfully " + CategoryID;
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        else{
            String message = "New event category creation failed, please try again";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

    }

    public String generateCategoryID(){

        StringBuilder generateRandomStr = new StringBuilder("C");

        for (int i = 0; i < 2; i++){
            generateRandomStr.append((char) (Math.random() * 26 + 65));

        }

        generateRandomStr.append("-");

        for(int i = 0; i < 4; i++){
            generateRandomStr.append((int) (Math.random() * 10));

        }

        return generateRandomStr.toString();
    }

    // Save new event category data to shared preferences.
    public void saveEventCategoryDataToSharedPreference(String CategoryID, String CategoryName,
                                                        String EventCount, Boolean isActive){
        SharedPreferences sP = getSharedPreferences(KeyStore.SP2_FILENAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.putString(KeyStore.CATEGORY_KEY, CategoryID);
        editor.putString(KeyStore.CATEGORY_NAME_KEY, CategoryName);
        editor.putString(KeyStore.EVENT_COUNT_KEY, EventCount);
        editor.putBoolean(KeyStore.IS_ACTIVE_KEY, isActive);
        editor.apply();
    }

    class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            myStringTokenizer(msg);

        }

        public void myStringTokenizer(String msg){
            try{
                StringTokenizer sT = new StringTokenizer(msg, ";");
                String categorySplice = sT.nextToken();
                // Only get what is after the ":" as a delimiter.
                String categoryName = categorySplice.substring(categorySplice.indexOf(":") + 1);
                String eventCount = sT.nextToken();
                String isActive = sT.nextToken();

                if(!isActive.equals("TRUE") && !isActive.equals("True") && !isActive.equals("FALSE") && !isActive.equals("False")){
                    throw new IllegalArgumentException("Invalid switch value");
                }

                try{
                    Integer.parseInt(eventCount);
                } catch (Exception e){
                    throw new IllegalArgumentException("Invalid event count");   // Can only be int.
                }

                EditTextCategoryName.setText(categoryName);
                EditTextEventCount.setText(eventCount);
                SwitchIsActive.setChecked(isActive.equals("TRUE"));

            } catch (Exception e){
                Toast.makeText(NewEventCategory.this,"Invalid SMS message", Toast.LENGTH_SHORT).show();
                EditTextCategoryName.setText("");
                EditTextEventCount.setText("");
                SwitchIsActive.setChecked(false);
            }

        }

    }

}