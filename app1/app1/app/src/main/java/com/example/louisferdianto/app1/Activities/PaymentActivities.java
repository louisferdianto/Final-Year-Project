package com.example.louisferdianto.app1.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.louisferdianto.app1.Data.DataRepository;
import com.example.louisferdianto.app1.Models.User;
import com.example.louisferdianto.app1.Services.PayPalConfig;
import com.example.louisferdianto.app1.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class PaymentActivities extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int PAYPAL_REQUEST_CODE = 7171;

    PayPalConfiguration config;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private Spinner qtySpinner;

    Button payBtn;
    String y,z,v;
    TextView total, amountperticket, title;
    String totalamount;
    String ticketprice, name,buyerName,buyerId, ticketQty,datetime,locationvenue,timeStart,timeEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_activities);
        config = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);

        payBtn = (Button) findViewById(R.id.payBtn);
        amountperticket = (TextView) findViewById(R.id.amountperticket);//harga tiket nett
        title = (TextView) findViewById(R.id.TitlePurchasedTicket);
        qtySpinner = (Spinner)findViewById(R.id.spinner);
        setCategorySpinner();

        if(firebaseUser != null) {


            DataRepository dataRepository = new DataRepository();
            dataRepository.getInstance().setUserName(firebaseUser.getUid());

            dataRepository.getUserAccountDetails(firebaseUser.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User userAccount = documentSnapshot.toObject(User.class);
                    buyerName = userAccount.getUserName();
                    buyerId = userAccount.getUserId();
                }
            });
        }

        Button backBtn = (Button) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PaymentActivities.this, BottomNavigationActivity.class);
                startActivity(i);
            }
        });

        ticketprice = getIntent().getExtras().getString("ticketprice");
        name = getIntent().getExtras().getString("eventName");
        locationvenue = getIntent().getExtras().getString("locationvenue");
        datetime = getIntent().getExtras().getString("dateEvent");
        timeEnd = getIntent().getExtras().getString("endTime");
        timeStart = getIntent().getExtras().getString("startTime");

        title.setText(name);
        amountperticket.setText(ticketprice);

        total = (TextView) findViewById(R.id.totalamount);

    }
    public void setCategorySpinner()
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.numberQtyTicket, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qtySpinner.setAdapter(adapter);
        qtySpinner.setOnItemSelectedListener(this);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        v = amountperticket.getText().toString();

        int priceperpax = new Integer(v);
        int qtyticket = new Integer(text);
        ticketQty = String.valueOf(qtyticket);
        final int totalprice = qtyticket * priceperpax;
        total.setText(String.valueOf(totalprice));
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(PaymentActivities.this, "Please select",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private void processPayment() {
        totalamount = total.getText().toString();
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(totalamount)), "MYR", "Ticket Price",
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT,payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK)
            {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null)
                {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);

                        //Starting a new activity for the payment details and also putting the payment details with intent
                        startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", totalamount)
                                .putExtra("eventName", name)
                                .putExtra("buyerName",buyerName)
                                .putExtra("ticketQty", ticketQty)
                                .putExtra("buyerId",buyerId)
                                .putExtra("locationvenue",locationvenue)
                        .putExtra("dateEvent",datetime)
                        .putExtra("startTime",timeStart)
                        .putExtra("endTime",timeEnd));

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

}