package com.example.louisferdianto.app1.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.louisferdianto.app1.Fragments.EventDetailFragment;
import com.example.louisferdianto.app1.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.annotation.Nullable;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>{

    private List<String> list_item ;
    public Context mcontext;
    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    private ListenerRegistration mQuery;


    public SearchAdapter(List<String> list_item, Context mcontext) {
        this.list_item = list_item;
        this.mcontext = mcontext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_event, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.titlename.setText(list_item.get(position));

    }

    @Override
    public int getItemCount() {
        return list_item.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView titlename;

        public MyViewHolder(View itemView) {
            super(itemView);
            titlename = (TextView) itemView.findViewById(R.id.event_list_title);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("TAG", list_item.get(getAdapterPosition()));
            mQuery = mFirestore.collection("Events").whereEqualTo("title",list_item.get(getAdapterPosition()))
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Log.d("TAG",snapshot.getId());
                                Bundle bundle = new Bundle();
                                bundle.putString(EventDetailFragment.KEY_RESTAURANT_ID,snapshot.getId());

                                EventDetailFragment eventDetailFragment = new EventDetailFragment();
                                eventDetailFragment.setArguments(bundle);
                                FragmentManager manager = ((AppCompatActivity)mcontext).getSupportFragmentManager();
                                manager.beginTransaction().replace(R.id.content, eventDetailFragment).commit();
                            }
                        }
                    });
        }

    }
}