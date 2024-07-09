package com.example.a3;

import androidx.lifecycle.AndroidViewModel;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.assignment_1_michael_cetrola.Event;
import com.example.assignment_1_michael_cetrola.EventCategory;

import java.util.List;

public class EMAViewModel extends AndroidViewModel {

    private EMARepository repository;
    private LiveData<List<Event>> allEventsLiveData;
    private LiveData<List<EventCategory>> allEventCategoryLiveData;

    public EMAViewModel(@NonNull Application application){
        super(application);
        repository = new EMARepository(application);
        allEventsLiveData = repository.getAllEvents();
        allEventCategoryLiveData = repository.getAllEventCategory();
    }

    public LiveData<List<Event>> getAllEvents(){
        return allEventsLiveData;
    }

    public LiveData<List<EventCategory>> getAllEventCategory(){
        return allEventCategoryLiveData;
    }

    public void insertEvent(Event event){
        repository.insert(event);
    }

    public void insertEventCategory(EventCategory eventCategory){
        repository.insert(eventCategory);
    }

    public void deleteAllEvent(){
        repository.deleteAllEvent();
    }

    public void deleteAllEventCategory(){
        repository.deleteAllEventCategory();
    }

    public void updateEvent(Event event){
        repository.updateEvent(event);
    }

    public void updateEventCategory(EventCategory eventCategory){
        repository.updateEventCategory(eventCategory);
    }

}
