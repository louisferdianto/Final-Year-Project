package com.example.louisferdianto.app1.ViewModel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.example.louisferdianto.app1.Data.FirebaseQueryLiveData;
import com.example.louisferdianto.app1.Filters;
import com.example.louisferdianto.app1.Models.Event;
import com.example.louisferdianto.app1.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EventListViewModel extends ViewModel{

    private static String TAG = "ListViewModel";
    private FirebaseFirestore mFirestore;
    private Query documentReference;
    private List<Event> mList = new ArrayList<>();
    private boolean mIsSigningIn;
    private Filters mFilters;

    @NonNull
    public LiveData<List<Event>> getEventListLiveData(){
        mFirestore = FirebaseFirestore.getInstance();
        documentReference = mFirestore.collection("Events");

        FirebaseQueryLiveData mLiveData = new FirebaseQueryLiveData(documentReference);

        LiveData<List<Event>> mEventLiveData = Transformations.map(mLiveData, new Deserializer());

        return mEventLiveData;
    }


    public EventListViewModel() {
        mIsSigningIn = false;
    }

    public boolean getIsSigningIn() {
        return mIsSigningIn;
    }

    public void setIsSigningIn(boolean mIsSigningIn) {
        this.mIsSigningIn = mIsSigningIn;
    }

    public Filters getFilters() {
        return mFilters;
    }

    public void setFilters(Filters mFilters) {
        this.mFilters = mFilters;
    }
    private class Deserializer implements Function<QuerySnapshot, List<Event>> {

        @Override
        public List<Event> apply(QuerySnapshot dataSnapshot) {
            mList.clear();
            for (DocumentSnapshot document : dataSnapshot.getDocuments()) {
                mList.add(document.toObject(Event.class));
            }
            return mList;
        }
    }
}
