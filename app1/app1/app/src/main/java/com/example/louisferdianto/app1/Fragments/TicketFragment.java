package com.example.louisferdianto.app1.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.louisferdianto.app1.Adapters.TicketAdapter;
import com.example.louisferdianto.app1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TicketFragment extends Fragment implements TicketAdapter.OnTicketSelectedListener{
    private static final String TAG = "Ticket Fragment";
    private Query mQuery;
    private TicketAdapter mAdapter;
    private RecyclerView mTicketRecycler;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ViewGroup mEmptyView;

    TicketPreviewFragment dialog;

    public TicketFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ticket, container, false);
        ButterKnife.bind(TicketFragment.this.getActivity());

        mEmptyView = (ViewGroup)v.findViewById(R.id.view_empty_ticket);
        mTicketRecycler = (RecyclerView)v.findViewById(R.id.recycler_tickets);

        mAuthStateListener = firebaseAuth -> {
           if(user != null)
           {

           }
        };


        mFirestore = FirebaseFirestore.getInstance();
        mQuery = mFirestore.collection("Users").document(user.getUid()).collection("Ticket");

        mAdapter = new TicketAdapter(mQuery,this) {
            @Override
            protected void onDataChanged() {
                if (getItemCount() == 0) {
                    mTicketRecycler.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    mTicketRecycler.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
            }
        };
        mTicketRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mTicketRecycler.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
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

    @Override
    public void onTicketSelected(DocumentSnapshot ticket) {
        Bundle bundle = new Bundle();
        bundle.putString(TicketPreviewFragment.KEY_RESTAURANT_ID,ticket.getId());
        dialog = new TicketPreviewFragment();
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "TAG");

    }
}
