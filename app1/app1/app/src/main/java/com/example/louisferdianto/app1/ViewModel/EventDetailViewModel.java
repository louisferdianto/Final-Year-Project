package com.example.louisferdianto.app1.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.louisferdianto.app1.Data.EventDetailRepository;
import com.example.louisferdianto.app1.Models.Event;
import com.example.louisferdianto.app1.Resource;

public class EventDetailViewModel extends ViewModel{

    private EventDetailRepository repository ;
    private final MutableLiveData<String> id = new MutableLiveData<>();
    private LiveData<Resource<Event>> event;

    public EventDetailViewModel() {
    }

    public EventDetailViewModel(EventDetailRepository repository) {
        this.repository = repository;
        event = Transformations.switchMap(id, repository::event);
    }

    public EventDetailViewModel setEventId(final String id) {
        if (id == null) {
            return null;
        }
        this.id.setValue(id);
        return this;
    }

    public LiveData<Resource<Event>> event() {
        return event;
    }
}