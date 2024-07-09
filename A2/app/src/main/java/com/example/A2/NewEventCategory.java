package com.example.a2;

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
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import java.security.Key;
import java.util.ArrayList;
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

        Toolbar toolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

        try {

            int intEventCount = Integer.parseInt(EventCount);

            if(CategoryName.isEmpty()){
                String message = "The event name cannot be blank.";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }

            else if (CategoryName.matches("[a-zA-Z]*") || CategoryName.matches("[a-zA-Z0-9 ]*") && !Objects.equals(CategoryName, "")) {

                EventCategory eventCategory = new EventCategory(CategoryID, CategoryName, intEventCount, IsActive);

                SharedPreferences sP = getSharedPreferences(KeyStore.SP6_FILENAME, MODE_PRIVATE);
                String eventCategoryJson = sP.getString(KeyStore.SP7_FILENAME, "");

                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<EventCategory>>() {
                }.getType();
                ArrayList<EventCategory> eventCategoryDataList = gson.fromJson(eventCategoryJson, type);

                if (eventCategoryDataList == null) {
                    eventCategoryDataList = new ArrayList<>();
                }

                eventCategoryDataList.add(eventCategory);
                String updatedJSON = gson.toJson(eventCategoryDataList);
                SharedPreferences.Editor editor = sP.edit();
                editor.putString(KeyStore.SP7_FILENAME, updatedJSON);
                editor.apply();

                EditTextCategoryID.setText(CategoryID);

                String message = "New Event Category saved successfully.";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                String message = "Invalid Category Name input, please try again.";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }

        catch (IllegalArgumentException e){
            String message = "Invalid input, please try again";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
//
//        if (CategoryName.matches("")){
//            String message = "The category name cannot be blank";
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//        }
//
//        else if (EventCount.matches("")){
//            String message = "The event count cannot be blank";
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//        }
//
//        else if (!Objects.equals(CategoryName, "") && !Objects.equals(EventCount, "") &&
//                IsActive.equals(true) || IsActive.equals(false)){
//            // Save new event category data to shared preferences.
//            saveEventCategoryDataToSharedPreference(CategoryID, CategoryName, EventCount, IsActive);
//            EditTextCategoryID.setText(CategoryID); // Pre-fill with the auto-generated code.
//            String message = "Category saved successfully " + CategoryID;
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//        }
//
//        else{
//            String message = "New event category creation failed, please try again";
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//        }

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
//    public void saveEventCategoryDataToSharedPreference(String CategoryID, String CategoryName,
//                                                        String EventCount, Boolean isActive){
//        SharedPreferences sP = getSharedPreferences(KeyStore.SP2_FILENAME, MODE_PRIVATE);
//        SharedPreferences.Editor editor = sP.edit();
//        editor.putString(KeyStore.CATEGORY_KEY, CategoryID);
//        editor.putString(KeyStore.CATEGORY_NAME_KEY, CategoryName);
//        editor.putString(KeyStore.EVENT_COUNT_KEY, EventCount);
//        editor.putBoolean(KeyStore.IS_ACTIVE_KEY, isActive);
//        editor.apply();
//    }

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