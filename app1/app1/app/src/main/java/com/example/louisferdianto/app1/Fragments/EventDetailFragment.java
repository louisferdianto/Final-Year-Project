package com.example.louisferdianto.app1.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.louisferdianto.app1.Activities.PaymentActivities;
import com.example.louisferdianto.app1.Data.EventDetailRepository;
import com.example.louisferdianto.app1.Models.Event;
import com.example.louisferdianto.app1.R;
import com.example.louisferdianto.app1.ViewModel.EventDetailViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class EventDetailFragment extends Fragment implements EventListener<DocumentSnapshot>{

    Button purchase;
    TextView desc, price, name,location,start,end,date;
    String locationname;
    String ticketprice, eventName,locationVenue,dateEvent,startEvent,endEvent;

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    ImageView photo;
    String eventsId;
    private FirebaseFirestore mFirestore;
    private DocumentReference mEventRef;
    public static final String KEY_RESTAURANT_ID = "key_restaurant_id";
    private ListenerRegistration mEventRegistration;

    private EventDetailViewModel viewModel;
    private EventDetailRepository repository;
    private OnFragmentInteractionListener mListener;

    public EventDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event_detail, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        desc = (TextView)v.findViewById(R.id.detailDesc);
        price = (TextView)v.findViewById(R.id.detailPrice);
        name = (TextView) v.findViewById(R.id.detailName);
        location = (TextView)v.findViewById(R.id.detailLocation);
        photo = (ImageView)v.findViewById(R.id.detailImage);
        start = (TextView)v.findViewById(R.id.detailStart);
        end = (TextView)v.findViewById(R.id.detailEnd);
        date = (TextView)v.findViewById(R.id.detailDate);
        mAuthStateListener = firebaseAuth -> {
            if (user != null) {

            }
        };
        purchase = (Button)v.findViewById(R.id.purchaseBtn);
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),PaymentActivities.class);

                i.putExtra("ticketprice",ticketprice);
                i.putExtra("eventName",eventName);
                i.putExtra("locationvenue", locationVenue);
                i.putExtra("dateEvent",dateEvent);
                i.putExtra("startTime",startEvent);
                i.putExtra("endTime",endEvent);

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });


        viewModel = ViewModelProviders.of(this).get(EventDetailViewModel.class);
        ButterKnife.bind(EventDetailFragment.this.getActivity());
        mFirestore = FirebaseFirestore.getInstance();
        eventsId = getArguments().getString(KEY_RESTAURANT_ID, null);
        if (eventsId == null) {
            throw new IllegalArgumentException("Must pass extra " + KEY_RESTAURANT_ID);
        }

        mEventRef = mFirestore.collection("Events").document(eventsId);


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        mEventRegistration = mEventRef.addSnapshotListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mEventRegistration != null) {
            mEventRegistration.remove();
            mEventRegistration = null;
        }
    }

    private void onEventsLoaded(Event event) {

        name.setText(event.getTitle());
        price.setText(event.getPrice());
        desc.setText(event.getDesc());
        location.setText(event.getLocation());
        locationname = location.getText().toString();
        Glide.with(photo.getContext())
                .load(event.getPhoto())
                .into(photo);
        start.setText(event.getTimeStart());
        end.setText(event.getTimeEnd());
        date.setText(event.getDate());


        ticketprice = event.getPrice();
        eventName = event.getTitle();
        locationVenue = event.getLocation();
        dateEvent = event.getDate();
        startEvent = event.getTimeStart();
        endEvent = event.getTimeEnd();
    }

    @Override
    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
        if (e != null) {
            Log.w("TAG", "restaurant:onEvent", e);
            return;
        }
        onEventsLoaded(documentSnapshot.toObject(Event.class));
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onResume(){
        super.onResume();
        Log.d("TAG", "onResume");
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d("TAG", "onPause");
        if(mAuthStateListener != null){
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}
