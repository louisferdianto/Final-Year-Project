package com.example.louisferdianto.app1.Data;

import com.example.louisferdianto.app1.Models.Event;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

public class EventDetailRepository {

    private CollectionReference events;

    public EventDetailRepository(CollectionReference events) {
        this.events = events;
    }

    public DocumentLiveData<Event> event(final String id) {
        if (id == null) {
            return null;
        }

        final DocumentReference restaurantRef = events.document(id);
        DocumentLiveData<Event> data = new DocumentLiveData<>(restaurantRef, Event.class);
        restaurantRef.addSnapshotListener(data);
        return data;
    }
}