package com.example.a2;

import android.content.SharedPreferences;

public class Event {

    private String eventID;
    private String eventName;
    private String categoryID;
    private int eventCount;
    private boolean isActive;

    public Event(String eventID, String eventName, String categoryID, int eventCount, boolean isActive) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.categoryID = categoryID;
        this.eventCount = eventCount;
        this.isActive = isActive;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}
