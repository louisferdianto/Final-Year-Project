package com.example.louisferdianto.app1.Adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.louisferdianto.app1.Models.Ticket;
import com.example.louisferdianto.app1.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import butterknife.ButterKnife;

public class TicketAdapter extends FirestoreAdapter<TicketAdapter.ViewHolder> {

    public interface OnTicketSelectedListener {

        void onTicketSelected(DocumentSnapshot restaurant);
    }

    private OnTicketSelectedListener mListener;


    public TicketAdapter(Query query, OnTicketSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new TicketAdapter.ViewHolder(inflater.inflate(R.layout.ticket_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView textTicketID;
        TextView textEventName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void bind(final DocumentSnapshot snapshot, final OnTicketSelectedListener listener) {
            Ticket ticket = snapshot.toObject(Ticket.class);
            Resources resources = itemView.getResources();

            view = itemView;
            textTicketID = itemView.findViewById(R.id.ticket_ID);
            textEventName = itemView.findViewById(R.id.ticket_eventName);

            textTicketID.setText(ticket.getPaymentId());
            textEventName.setText(ticket.getEventName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onTicketSelected(snapshot);
                    }
                }
            });
        }
    }
}
