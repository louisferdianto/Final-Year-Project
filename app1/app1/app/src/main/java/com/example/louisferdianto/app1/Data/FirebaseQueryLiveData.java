package com.example.louisferdianto.app1.Data;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.louisferdianto.app1.Models.Event;
import com.example.louisferdianto.app1.Models.Model;
import com.example.louisferdianto.app1.Resource;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseQueryLiveData extends LiveData<QuerySnapshot> {

   private static final String TAG = "FirebaseQueryLiveData";
    private final MyValueEventListener listener = new MyValueEventListener();
    private List<Event> mQueryValuesList = new ArrayList<>();
    private ListenerRegistration listenerRegistration;
    private Query query;

    public FirebaseQueryLiveData(Query query) {
        this.query = query;
    }

    @Override
    protected void onActive() {
        Log.d(TAG, "onActive");
        if (listenerRegistration == null )
            listenerRegistration = query.addSnapshotListener(listener);
    }
        @Override
        protected void onInactive () {
            // Listener removal is schedule on a two second delay

            Log.d(TAG, "onInactive: ");
            if (listenerRegistration != null)
                listenerRegistration.remove();
        }


        class MyValueEventListener implements EventListener<QuerySnapshot>{

            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, "Can't listen to doc snapshots: " + snapshots + ":::" + e.getMessage());
                    return;
                }
                setValue(snapshots);
                for (DocumentSnapshot document : snapshots.getDocuments()) {
                    mQueryValuesList.add(document.toObject(Event.class));
                }
            }
        }

}
/*private final Query query;
    private final Class<T> type;
    private ListenerRegistration registration;

    public FirebaseQueryLiveData(Query query, Class<T> type) {
        this.query = query;
        this.type = type;
    }
    @Override
    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
        if (e != null) {
            setValue(new Resource<>(e));
            return;
        }
        setValue(new Resource<>(documentToList(queryDocumentSnapshots)));
    }
    @Override
    protected void onActive() {
        super.onActive();
        registration = query.addSnapshotListener(this);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if (registration != null) {
            registration.remove();
            registration = null;
        }
    }
    @NonNull
    private List<T> documentToList(QuerySnapshot snapshots) {
        final List<T> retList = new ArrayList<>();
        if (snapshots.isEmpty()) {
            return retList;
        }

        for (DocumentSnapshot document : snapshots.getDocuments()) {
            retList.add(document.toObject(type).withId(document.getId()));
        }

        return retList;
    }*/

