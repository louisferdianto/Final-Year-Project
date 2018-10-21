package com.example.louisferdianto.app1.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.louisferdianto.app1.Data.DataRepository;
import com.example.louisferdianto.app1.Models.Ticket;
import com.example.louisferdianto.app1.Models.User;
import com.example.louisferdianto.app1.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;

public class PaymentDetails extends AppCompatActivity {
    String eventName, buyerName, buyerId;
    FirebaseFirestore db;
    String ticketid, ticketqty, ticketamount,currentDateTimeString,locationVenue,dateEvent,startTime,endTime;
    Button homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        Intent intent = getIntent();
        eventName = getIntent().getExtras().getString("eventName");
        buyerName = getIntent().getExtras().getString("buyerName");
        buyerId = getIntent().getExtras().getString("buyerId");
        ticketqty = getIntent().getExtras().getString("ticketQty");

        locationVenue = getIntent().getExtras().getString("locationvenue");
        dateEvent = getIntent().getExtras().getString("dateEvent");
        startTime = getIntent().getExtras().getString("startTime");
        endTime = getIntent().getExtras().getString("endTime");

        try {
            JSONObject jsonDetails = new JSONObject(intent.getStringExtra("PaymentDetails"));

            //Displaying payment details
            showDetails(jsonDetails.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        saveTicketDetailsToFirestore();
        homeBtn = (Button)findViewById(R.id.paymentDetails_homeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PaymentDetails.this, BottomNavigationActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
            }
        });
    }

    private void showDetails(JSONObject jsonDetails, String paymentAmount) throws JSONException {
        //Views
        TextView paymentid = (TextView) findViewById(R.id.paymentId);
        TextView textViewStatus = (TextView) findViewById(R.id.paymentStatus);
        TextView textViewAmount = (TextView) findViewById(R.id.paymentAmount);
        TextView textViewEventName = (TextView) findViewById(R.id.paidEventName);
        TextView textViewBuyerName = (TextView) findViewById(R.id.buyerName);
        TextView textViewTicketQty = (TextView) findViewById(R.id.ticketQty);
        TextView textDateTime = (TextView) findViewById(R.id.ticketDateTimePurchased);

            //Showing the details from json object
            paymentid.setText(jsonDetails.getString("id"));
            textViewStatus.setText(jsonDetails.getString("state"));
            textViewAmount.setText("RM"+paymentAmount);
            textViewEventName.setText(eventName);
            textViewBuyerName.setText(buyerName);
            textViewTicketQty.setText(ticketqty);

            ticketid = paymentid.getText().toString();
            ticketamount = textViewAmount.getText().toString();
            currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            textDateTime.setText(currentDateTimeString);
    }

    private void saveTicketDetailsToFirestore()
    {
        db = FirebaseFirestore.getInstance();
        Ticket ticket = new Ticket(ticketid,ticketamount,buyerName,eventName,ticketqty, currentDateTimeString,locationVenue,dateEvent,startTime,endTime);
        DocumentReference ticketRef = db.collection("Users").document(buyerId)
                .collection("Ticket").document(ticketid);
        ticketRef.set(ticket).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Ticket Created", Toast.LENGTH_SHORT).show();
            }
        });

    }
}