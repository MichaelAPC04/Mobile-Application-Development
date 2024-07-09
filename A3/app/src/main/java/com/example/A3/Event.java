package com.example.a3;

import android.content.SharedPreferences;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "event")
public class Event {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "eventID")
    private String eventID;

    @ColumnInfo(name = "eventName")
    private String eventName;

    @ColumnInfo(name = "categoryID")
    private String categoryID;

    @ColumnInfo(name = "eventCount")
    private int eventCount;

    @ColumnInfo(name = "isActive")
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
