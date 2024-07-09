package com.example.a3;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.assignment_1_michael_cetrola.Event;
import com.example.assignment_1_michael_cetrola.EventCategory;

import java.util.List;

@Dao
public interface emaDAO {

    @Query("select * from event")
    LiveData<List<Event>> getAllEvents();

    @Insert
    void addEvent(Event event);

    @Query("select * from eventCategory")
    LiveData<List<EventCategory>> getAllEventCategory();

    @Insert
    void addEventCategory(EventCategory eventCategory);

    @Query("DELETE FROM event")
    void deleteAllEvents();

    @Query("DELETE FROM eventCategory")
    void deleteAllEventCategory();

    @Update
    void updateEvent(Event event);

    @Update
    void updateEventCategory(EventCategory eventCategory);

}
