package com.example.louisferdianto.app1.Adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.louisferdianto.app1.Models.Event;
import com.example.louisferdianto.app1.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by louisferdianto on 09/03/2018.
 */

public  class eventsAdapter extends FirestoreAdapter<eventsAdapter.ViewHolder> {
    private List<? extends Event> mEventlist ;

    public interface OnEventSelectedListener {

        void onEventSelected(DocumentSnapshot restaurant);
    }

    private OnEventSelectedListener mListener;


    public void setEventList(final List<? extends Event> eventList){
        mEventlist = eventList;
        notifyDataSetChanged();
    }

    public eventsAdapter(Query query, OnEventSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.card_event, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView imagePoster;
        TextView textName;
        TextView textLocation;
        TextView textDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final DocumentSnapshot snapshot, final OnEventSelectedListener listener) {
            Event event = snapshot.toObject(Event.class);
            Resources resources = itemView.getResources();

            view = itemView;
            imagePoster = itemView.findViewById(R.id.event_poster);
            textName = itemView.findViewById(R.id.event_item_name);
            textDate = itemView.findViewById(R.id.event_item_date);
            textLocation = itemView.findViewById(R.id.event_item_city);
            Glide.with(imagePoster.getContext())
                    .load(event.getPhoto())
                    .into(imagePoster);

            textName.setText(event.getTitle());
            textDate.setText(event.getDate());
            textLocation.setText(event.getLocation());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onEventSelected(snapshot);
                    }
                }
            });
        }
    }
}
