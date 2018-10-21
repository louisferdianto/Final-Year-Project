package com.example.louisferdianto.app1.Activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.louisferdianto.app1.Fragments.MainFragment;
import com.example.louisferdianto.app1.R;

public class RegisterActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
