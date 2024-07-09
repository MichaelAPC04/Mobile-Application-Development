package com.example.a1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DashboardWindow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_window);
    }

    // Navigate between the AddEvent and NewEventCategory classes
    public void onNewEventCategory(View view){
        Intent newEventCategoryIntent = new Intent(this, NewEventCategory.class);
        startActivity(newEventCategoryIntent);

    }

    public void onAddEvent(View view){
        Intent onAddEventIntent = new Intent(this, AddEvent.class);
        startActivity(onAddEventIntent);

    }

}