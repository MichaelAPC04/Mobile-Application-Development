package com.example.a3;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.Key;
import androidx.constraintlayout.motion.widget.KeyTimeCycle;
import androidx.core.view.GestureDetectorCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;   // Added import

import androidx.annotation.NonNull;   // Added import
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Update;

import com.example.assignment_1_michael_cetrola.provider.EMAViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DashboardWindow extends AppCompatActivity {

    DrawerLayout drawerlayout;
    NavigationView navigationView;
    EditText etEventID, etEventName, etCategoryID, etTicketsAvailable;
    TextView tvEventType;
    Switch switchNewEventIsActive;
    String eventID, eventName, categoryID, ticketsAvailable;
    Boolean newEventIsActive;

    //Added below
    private EMAViewModel emaViewModel;
    MyRecyclerAdapter eventAdapter;
    MyRecyclerAdapterEventCategory eventCategoryAdapter;
    private GestureDetectorCompat mDector;
    private ScaleGestureDetector mScaleDector;
    List<EventCategory> eventCategoryList;
    List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        etEventID = findViewById(R.id.etEventID);
        etEventName = findViewById(R.id.etEventName);
        etCategoryID = findViewById(R.id.etCategoryID);
        etTicketsAvailable = findViewById(R.id.etTicketsAvailable);
        switchNewEventIsActive = findViewById(R.id.switchNewEventIsActive);
        tvEventType = findViewById(R.id.tvEventType);

        emaViewModel = new ViewModelProvider(this).get(EMAViewModel.class);

        emaViewModel.getAllEventCategory().observe(this, newData -> {
            eventCategoryList = new ArrayList<EventCategory>(newData);
        });

        CustomGestureDetector customGestureDetector = new CustomGestureDetector();
        mDector = new GestureDetectorCompat(this, customGestureDetector);
        mDector.setOnDoubleTapListener(customGestureDetector);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerlayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        FloatingActionButton fab = findViewById(R.id.fab);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

        loadEventCategories(null);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean checkIsActive = onAddEventBtn(null);
                if (checkIsActive){

                Snackbar.make(view, "Event saved", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences sP = getSharedPreferences(KeyStore.SP4_FILENAME, MODE_PRIVATE);
                                String eventJson = sP.getString(KeyStore.SP5_FILENAME, "");

                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<Event>>(){}.getType();
                                ArrayList<Event> eventDataList = gson.fromJson(eventJson, type);

                                if(eventDataList != null && eventDataList.size() > 0){
                                    eventDataList.remove(eventDataList.size() - 1);
                                    String updatedJson = gson.toJson(eventDataList);

                                    SharedPreferences.Editor editor = sP.edit();
                                    editor.putString(KeyStore.SP5_FILENAME, updatedJson);
                                    editor.apply();

                                    etEventID.getText().clear();
                                    etEventName.getText().clear();
                                    etCategoryID.getText().clear();
                                    etTicketsAvailable.getText().clear();
                                    switchNewEventIsActive.setChecked(false);

                                    Snackbar.make(v, "Event successfully removed", Snackbar.LENGTH_SHORT).show();
                                }

                            }
                        }).show();
            }}
        });

        View touchpad = findViewById(R.id.touchpad);
        touchpad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mDector.onTouchEvent(event);

                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.secondary_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onNewEventCategory(View view){
        Intent newEventCategoryIntent = new Intent(this, NewEventCategory.class);
        startActivity(newEventCategoryIntent);

    }

    public void onAddEvent(View view){
        Intent onAddEventIntent = new Intent(this, AddEvent.class);
        startActivity(onAddEventIntent);

    }

    public void onLoginWindow(View view){
        Intent logout = new Intent(this, LoginWindow.class);
        startActivity(logout);
        finish();
    }

    public void onFragmentListEvent(View view){
        Intent viewFragment = new Intent(this, ViewAllEvents.class);
        startActivity(viewFragment);
    }

    public void onFragmentListCategories(View view){
        Intent viewFragment = new Intent(this, ViewAllEventCategories.class);
        startActivity(viewFragment);
    }

    public boolean onAddEventBtn(View view){
        eventID = generateEventID();
        eventName = etEventName.getText().toString();
        categoryID = etCategoryID.getText().toString();
        ticketsAvailable = etTicketsAvailable.getText().toString();
        newEventIsActive = switchNewEventIsActive.isChecked();

        try {

            if(eventName.isEmpty()){
                String message = "The event name cannot be blank.";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return false;
            }

            int intTickets = Integer.parseInt(ticketsAvailable);

            emaViewModel = new ViewModelProvider(this).get(EMAViewModel.class);

            Event event = new Event(eventID, eventName, categoryID, intTickets, newEventIsActive);

//            ArrayList<EventCategory> listCategory;
//            Gson GSON = new Gson();
//
//            SharedPreferences sharedPreference = getSharedPreferences(KeyStore.SP6_FILENAME, Context.MODE_PRIVATE);
//            String json = sharedPreference.getString(KeyStore.SP7_FILENAME, null);
//
//            Type Type = new TypeToken<ArrayList<EventCategory>>(){}.getType();
//            listCategory = GSON.fromJson(json, Type);

            final boolean[] check = {false};

//            EventCategory eventCategory = emaViewModel.getAllEvents();
//
//            if(listCategory != null){
//                for(EventCategory listcategory : listCategory){
//                    if(listcategory.getCategoryID().equals(categoryID)){
//                        check = true;
//                    }
//                }
//            }

            emaViewModel.getAllEventCategory().observe(this, new Observer<List<EventCategory>>() {
                @Override
                public void onChanged(List<EventCategory> events) {
                    for(EventCategory event : events){
                        if(event.getCategoryID().equals(categoryID)){
                            check[0] = true;
                            break;
                        }
                    }
                }
            });

            if(!check[0]){
                String message = "Inputted CategoryID does not match.";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return false;
            }

            if (eventName.matches("[a-zA-Z0-9 ]*") && !Objects.equals(eventName, "")) {

//                SharedPreferences sP = getSharedPreferences(KeyStore.SP4_FILENAME, MODE_PRIVATE);
//                String eventJSON = sP.getString(KeyStore.SP5_FILENAME, "");
//
//                Gson gson = new Gson();
//                Type type = new TypeToken<ArrayList<Event>>() {
//                }.getType();
//                ArrayList<Event> eventDataList = gson.fromJson(eventJSON, type);
//
//                if (eventDataList == null) {
//                    eventDataList = new ArrayList<>();
//                }
                emaViewModel.insertEvent(event);

//                eventDataList.add(event);
//                String updatedJSON = gson.toJson(eventDataList);
//                SharedPreferences.Editor editor = sP.edit();
//                editor.putString(KeyStore.SP5_FILENAME, updatedJSON);
//                editor.apply();

                etEventID.setText(eventID);

                String message = "New Event saved successfully.";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                // Increment +1
//                SharedPreferences SP = getSharedPreferences(KeyStore.SP6_FILENAME, MODE_PRIVATE);
//                String categoryJson = SP.getString(KeyStore.SP7_FILENAME, "");
//                Type TYPE = new TypeToken<ArrayList<EventCategory>>(){}.getType();
//                ArrayList<EventCategory> categoryArrayList = gson.fromJson(categoryJson, TYPE);
//
//                for(EventCategory category : categoryArrayList){
//                    if(category.getCategoryID().equals(categoryID)){
//                        category.setEventCount(category.getEventCount()+1);
//                        break;
//                    }
//
//                }

//                String updatedCategoryJson = gson.toJson(categoryArrayList);
//                SharedPreferences.Editor edit = SP.edit();
//                edit.putString(KeyStore.SP7_FILENAME, updatedCategoryJson);
//                edit.apply();

                for(EventCategory events : eventCategoryList){
                    if(events.getCategoryID().equals(categoryID)){
                        events.setEventCount(events.getEventCount() + 1);
                        emaViewModel.updateEventCategory(events);
                    }
                        return true;
                    }

            }

            else {
                String message = "Invalid name input, please try again.";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return false;
            }

        }

        catch (IllegalArgumentException e){
            String message = "Invalid input, please try again";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            return false;
        }

        return false;
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

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == R.id.item_id_clear_event_form){
            etEventID.getText().clear();
            etEventName.getText().clear();
            etCategoryID.getText().clear();
            etTicketsAvailable.getText().clear();
            switchNewEventIsActive.setChecked(false);
        }

        else if (item.getItemId() == R.id.item_id_delete_all_events){
            //removeEventData();
            emaViewModel.deleteAllEvent();
        }

        else if (item.getItemId() == R.id.item_id_delete_all_categories){
            //removeEventCategoryData();
            emaViewModel.deleteAllEventCategory();
        }

        return true;
    }

    private void removeEventData(){
        SharedPreferences sP = getSharedPreferences(KeyStore.SP4_FILENAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.remove(KeyStore.SP5_FILENAME);
        editor.apply();
    }

    private void removeEventCategoryData(){
        SharedPreferences sP = getSharedPreferences(KeyStore.SP6_FILENAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.remove(KeyStore.SP7_FILENAME);
        editor.apply();
    }

    public void loadEventCategories(View view){
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragmentContainerView3, new FragmentListCategory()).commit();
    }

    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // get the id of the selected item
            int id = item.getItemId();

            if (id == R.id.item_id_view_all_categories) {
                onFragmentListCategories(null);
            }
            else if (id == R.id.item_id_add_category) {
                onNewEventCategory(null);
            }
            else if (id == R.id.item_id_view_all_events) {
                onFragmentListEvent(null);
            }
            else if (id == R.id.item_id_logout) {
                onLoginWindow(null);
            }

            // close the drawer
            drawerlayout.closeDrawers();
            // tell the OS
            return true;
        }
    }

    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener{

        @Override
        public void onLongPress(@NonNull MotionEvent e){
            tvEventType.setText("onLongPress - clear all fields");
            etEventID.getText().clear();
            etEventName.getText().clear();
            etCategoryID.getText().clear();
            etTicketsAvailable.getText().clear();
            switchNewEventIsActive.setChecked(false);
            super.onLongPress(e);
        }

        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e){
            tvEventType.setText("onDoubleTap - save event");
            onAddEventBtn(null);
            return super.onDoubleTap(e);
        }

    }

}