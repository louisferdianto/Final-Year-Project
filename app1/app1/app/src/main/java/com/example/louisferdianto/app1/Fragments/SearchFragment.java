package com.example.louisferdianto.app1.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.louisferdianto.app1.R;
import com.example.louisferdianto.app1.Adapters.SearchAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private FirebaseFirestore mFirestore;

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private EditText editSearch;
    RecyclerView listView;
    private Query mQuery;
    private List<String> namesList = new ArrayList<>();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ViewGroup mEmptyView;
    String eventsId;
    SearchAdapter searchAdapter2;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(SearchFragment.this.getActivity());
        editSearch = (EditText) v.findViewById(R.id.search_field);
        listView = (RecyclerView) v.findViewById(R.id.searchRecyclerView);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setAdapter(new SearchAdapter(namesList,getContext()));
        mEmptyView = (ViewGroup)v.findViewById(R.id.view_empty_event);
        eventsId = getArguments().getString("category", null);

        mAuthStateListener = firebaseAuth -> {
            if (user != null) {

            }
        };
        mFirestore = FirebaseFirestore.getInstance();

        mQuery = mFirestore.collection("Events").whereEqualTo("categories",eventsId);

        mQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                namesList.clear();
                String x = null;
                if(documentSnapshots.isEmpty())
                {
                    listView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                }
                else
                {
                    for (DocumentSnapshot snapshot : documentSnapshots) {
                        namesList.add(snapshot.getString("title"));
                    }
                    searchAdapter2 = new SearchAdapter(namesList,getContext());
                    listView.setLayoutManager(new LinearLayoutManager(getContext()));
                    listView.setAdapter(searchAdapter2);
                }

            }
        });
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                s = s.toString().toLowerCase();

                final List<String> filteredList = new ArrayList<>();

                for (int i = 0; i < namesList.size(); i++) {

                    final String text = namesList.get(i).toLowerCase();
                    if (text.contains(s)) {

                        filteredList.add(namesList.get(i));
                        listView.setVisibility(View.VISIBLE);
                        mEmptyView.setVisibility(View.GONE);
                    }
                    if(text.isEmpty())
                    {
                        listView.setVisibility(View.GONE);
                        mEmptyView.setVisibility(View.VISIBLE);
                    }
                }
                listView.setLayoutManager(new LinearLayoutManager(getContext()));
                searchAdapter2 = new SearchAdapter(filteredList,getContext());
                listView.setAdapter(searchAdapter2);
                searchAdapter2.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
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