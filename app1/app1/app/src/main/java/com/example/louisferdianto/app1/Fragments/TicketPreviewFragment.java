package com.example.louisferdianto.app1.Fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.louisferdianto.app1.Models.Event;
import com.example.louisferdianto.app1.Models.Ticket;
import com.example.louisferdianto.app1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import javax.annotation.Nullable;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TicketPreviewFragment extends DialogFragment implements EventListener<DocumentSnapshot> {
    public static final String KEY_RESTAURANT_ID = "key_restaurant_id";
    private static final String TAG = "Ticket Preview Fragment";

    String ticketId;
    private FirebaseFirestore mFirestore;
    private DocumentReference ticketRef;
    private ListenerRegistration mTicketRegistration;

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    TextView ticketid, ticketqty, name,location,start,date;


    public TicketPreviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_ticket_preview, container, false);


        ticketid = (TextView)v.findViewById(R.id.preview_ticketId);
        ticketqty = (TextView)v.findViewById(R.id.preview_ticketQty);
        name = (TextView)v.findViewById(R.id.preview_eventName);
        location = (TextView)v.findViewById(R.id.preview_venueLocation);
        start = (TextView)v.findViewById(R.id.preview_timeStart);
        date = (TextView)v.findViewById(R.id.preview_dateEvent);

        ButterKnife.bind(TicketPreviewFragment.this.getActivity());
        mFirestore = FirebaseFirestore.getInstance();

        ticketId = getArguments().getString(KEY_RESTAURANT_ID, null);

        mAuthStateListener = firebaseAuth -> {
            if(user != null)
            {

            }
        };


        if (ticketId == null) {
            throw new IllegalArgumentException("Must pass extra " + KEY_RESTAURANT_ID);
        }
        ticketRef = mFirestore.collection("Users").document(user.getUid()).collection("Ticket").document(ticketId);


        return v;
    }
    private void onTicketLoaded(Ticket ticket) {

        name.setText(ticket.getEventName());
        ticketid.setText(ticket.getPaymentId());
        location.setText(ticket.getLocationVenue());
        ticketqty.setText(ticket.getTicketQty());
        date.setText(ticket.getDateEvent());
        start.setText(ticket.getStartTime());

    }
    @Override
    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            Log.w("TAG", "restaurant:onEvent", e);
            return;
        }
        onTicketLoaded(snapshot.toObject(Ticket.class));
    }
    @Override
    public void onStart() {
        super.onStart();
        mTicketRegistration = ticketRef.addSnapshotListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mTicketRegistration != null) {
            mTicketRegistration.remove();
            mTicketRegistration = null;
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume");
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause");
        if(mAuthStateListener != null){
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}
