package com.example.a3;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.assignment_1_michael_cetrola.Event;
import com.example.assignment_1_michael_cetrola.EventCategory;

import java.util.List;

public class EMARepository {
    private emaDAO emaDAO;
    private LiveData<List<Event>> allEventsLiveData;
    private LiveData<List<EventCategory>> allEventCategoryLiveData;

    EMARepository(Application application){
        EMADatabase db = EMADatabase.getDatabase(application);
        emaDAO = db.emaDAO();
        allEventsLiveData = emaDAO.getAllEvents();
        allEventCategoryLiveData = emaDAO.getAllEventCategory();
    }

    LiveData<List<Event>> getAllEvents(){
        return allEventsLiveData;
    }

    LiveData<List<EventCategory>> getAllEventCategory(){
        return allEventCategoryLiveData;
    }

    void insert(Event event){
        EMADatabase.databaseWriteExecutor.execute(() -> emaDAO.addEvent(event));
    }

    void insert(EventCategory eventCategory){
        EMADatabase.databaseWriteExecutor.execute(() -> emaDAO.addEventCategory(eventCategory));
    }

    void deleteAllEvent(){
        EMADatabase.databaseWriteExecutor.execute(() -> emaDAO.deleteAllEvents());
    }

    void deleteAllEventCategory(){
        EMADatabase.databaseWriteExecutor.execute(() -> emaDAO.deleteAllEventCategory());
    }

    void updateEvent(Event event){
        emaDAO.updateEvent(event);
    }

    void updateEventCategory(EventCategory eventCategory){
        EMADatabase.databaseWriteExecutor.execute(() -> emaDAO.updateEventCategory(eventCategory));
    }

}
