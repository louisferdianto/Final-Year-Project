package com.example.louisferdianto.app1.Fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.louisferdianto.app1.Data.DataRepository;
import com.example.louisferdianto.app1.Filters;
import com.example.louisferdianto.app1.Models.Event;
import com.example.louisferdianto.app1.R;
import com.example.louisferdianto.app1.ViewModel.EventListViewModel;
import com.example.louisferdianto.app1.Adapters.eventsAdapter;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MainFragment extends Fragment implements
        eventsAdapter.OnEventSelectedListener{
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private eventsAdapter mAdapter;
    private RecyclerView mEventRecycler;
    private EventListViewModel mModel ;
    private static final String TAG = "MainFragment";
    private static final String ANONYMOUS = "anonymous";
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    ViewGroup mEmptyView;
    Spinner categorySpinner;
    private DialogFilterFragment mFilterDialog;
CardView x;
    // @BindView(R.id.text_current_search)
    //TextView mCurrentSearchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mModel = ViewModelProviders.of(getActivity()).get(EventListViewModel.class);
        mEmptyView = (ViewGroup)v.findViewById(R.id.view_empty_event);

        mAuthStateListener = firebaseAuth -> {

            FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                DataRepository.getInstance().setUserName(user.getEmail());
            } else {
                Log.d(TAG, "user is null");
                DataRepository.getInstance().setUserName(ANONYMOUS);
                // Choose authentication providers
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
                // Create and launch sign-in intent
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(providers)
                                .setLogo(R.mipmap.ic_launcher)
                                .build(),
                        RC_SIGN_IN);
            }
        };

        ButterKnife.bind(MainFragment.this.getActivity());
        mEventRecycler = (RecyclerView)v.findViewById(R.id.recycler_events);
        initFirestore();
        initRecyclerView();
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mEventRecycler.setAdapter(mAdapter);
        if(mModel != null){
            LiveData<List<Event>> liveData = mModel.getEventListLiveData();

            liveData.observe(getActivity(), (List<Event> mEntities) -> {
                mAdapter.setEventList(mEntities);
            });
        }
    }

    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w("TAG", "No query, not initializing RecyclerView");
        }

        mAdapter = new eventsAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                if (getItemCount() == 0) {
                    mEventRecycler.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);

                } else {
                    mEventRecycler.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
            }
        };

        mEventRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mEventRecycler.setAdapter(mAdapter);
    }

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
        mQuery = mFirestore.collection("Events");
    }



    @Override
    public void onStart() {
        super.onStart();
        //onFilter(mModel.getFilters());

        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    @Override
    public void onEventSelected(DocumentSnapshot event) {
        Bundle bundle = new Bundle();
        bundle.putString(EventDetailFragment.KEY_RESTAURANT_ID,event.getId());

        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        eventDetailFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, eventDetailFragment).commit();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

    private void startSignIn() {
        // Sign in with FirebaseUI
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()))
                .setIsSmartLockEnabled(false)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);
    }
}
