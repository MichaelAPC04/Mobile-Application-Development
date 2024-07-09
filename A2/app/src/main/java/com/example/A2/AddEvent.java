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

import java.security.Key;
import java.util.Objects;
import java.util.StringTokenizer;

public class AddEvent extends AppCompatActivity {

    EditText EditTextEventID, EditTextEventName, EditTextEventCategoryID, EditTextTicketsAvailable;
    Switch EventIsActive;
    String TextEventID, EventName, EventCategoryID, TicketsAvailable, CategoryIDRestored;
    Boolean IsActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        EditTextEventID = findViewById(R.id.editTextEventID);
        EditTextEventName = findViewById(R.id.editTextEventName);
        EditTextEventCategoryID = findViewById(R.id.editTextEventCategoryID);
        EditTextTicketsAvailable = findViewById(R.id.editTextTicketsAvailable);
        EventIsActive = findViewById(R.id.switchEventIsActive);

        // Call shared preference categoryID, save event to that specific category only
        SharedPreferences sP = getSharedPreferences(KeyStore.SP2_FILENAME, MODE_PRIVATE);
        CategoryIDRestored = sP.getString(KeyStore.CATEGORY_KEY, "");

        if(!CategoryIDRestored.isEmpty()) {
            EditTextEventCategoryID.setText(CategoryIDRestored);
        }

        // Sms permissions
        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.RECEIVE_SMS,
                android.Manifest.permission.READ_SMS
        }, 0);

        AddEvent.MyBroadCastReceiver myBroadCastReceiver = new AddEvent.MyBroadCastReceiver();
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER), RECEIVER_EXPORTED);

    }

    public void onAddEventClick(View view){
        TextEventID = generateEventID();
        EventCategoryID = EditTextEventCategoryID.getText().toString();
        EventName = EditTextEventName.getText().toString();
        TicketsAvailable = EditTextTicketsAvailable.getText().toString();
        IsActive = EventIsActive.isChecked();

        if(EventName.matches("")){
            String message = "The event name cannot be empty";
            Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
        }

        else if(EventCategoryID.matches("")){
            String message = "The event category ID cannot be empty";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        else if(TicketsAvailable.matches("")){
            String message = "The tickets available cannot be empty";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        else if (!Objects.equals(EventName, "") && !Objects.equals(TicketsAvailable, "")
                && IsActive.equals(true) || IsActive.equals(false)){
            saveAddEventDataToSharedPreferences(TextEventID, EventName, EventCategoryID,
                    TicketsAvailable, IsActive);
            EditTextEventID.setText(TextEventID);   // Save to SP, display auto-generated Event ID
            String message = "Event saved: " + TextEventID + " to " + EventCategoryID;
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        else{
            String message = "New event creation failed, please try again";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

    }

    public String generateEventID(){
        StringBuilder generateRandomStr = new StringBuilder("E");

        for (int i = 0; i < 2; i++){
            generateRandomStr.append((char) (Math.random() * 26 + 65));   // ASCII capitals only
        }

        generateRandomStr.append("-");

        for(int i = 0; i < 5; i++){
            generateRandomStr.append((int) (Math.random() * 10));   // Random ints 0-9
        }

        return generateRandomStr.toString();

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
                String addEventSplice = sT.nextToken();   // Only get what is after the ":" from SMS
                String addEvent = addEventSplice.substring(addEventSplice.indexOf(":") + 1);
                String categoryID = sT.nextToken();
                String ticketsAvailable = sT.nextToken();
                String isActive = sT.nextToken();

                if(!isActive.equals("TRUE") && !isActive.equals("True") && !isActive.equals("FALSE") && !isActive.equals("False")){
                    throw new IllegalArgumentException("Invalid switch value");
                }

                try{
                    Integer.parseInt(ticketsAvailable);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid ticket count");   // Has to be int
                }

                EditTextEventName.setText(addEvent);
                EditTextEventCategoryID.setText(categoryID);
                EditTextTicketsAvailable.setText(ticketsAvailable);
                EventIsActive.setChecked(isActive.equals("TRUE"));

            } catch (IllegalArgumentException e) {
                Toast.makeText(AddEvent.this,"Invalid SMS message", Toast.LENGTH_SHORT).show();
                EditTextEventName.setText("");
                EditTextTicketsAvailable.setText("");
                EventIsActive.setChecked(false);
            }

        }

    }

    // Save new event data to shared preferences.
    public void saveAddEventDataToSharedPreferences(String EventID, String EventName,
                                                    String EventCategoryID, String TicketsAvailable,
                                                    Boolean IsActive){
        SharedPreferences sP = getSharedPreferences(KeyStore.SP3_FILENAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.putString(KeyStore.EVENT_ID_KEY, EventID);
        editor.putString(KeyStore.EVENT_NAME_KEY, EventName);
        editor.putString(KeyStore.EVENT_CATEGORY_ID_KEY, EventCategoryID);
        editor.putString(KeyStore.TICKETS_AVAILABLE_KEY, TicketsAvailable);
        editor.putBoolean(KeyStore.EVENT_IS_ACTIVE, IsActive);
        editor.apply();

    }

}