package com.example.louisferdianto.app1.Data;

import android.util.Log;
import android.widget.TextView;

import com.example.louisferdianto.app1.Models.Event;
import com.example.louisferdianto.app1.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DataRepository {

    private static  DataRepository sInstance;

    private String mUsername;
    private FirebaseFirestore mFirestore;
    private CollectionReference mMessageDbReference;
    public TextView userName, userEmail, userPhone;
    public DocumentReference docRef;
    public DataRepository() {
        mFirestore = FirebaseFirestore.getInstance();
        mMessageDbReference = mFirestore.collection("Users");
    }
    public static DataRepository getInstance() {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository();
                }
            }
        }
        return sInstance;
    }

    public Task<DocumentSnapshot> getUserAccountDetails(String userid) {
        docRef = mMessageDbReference.document(userid); // Users are referenced by email
        return docRef.get();
    }

    public void setUserName(String userName){
        mUsername = userName;
    }

    public String getUserName(){
        return mUsername;
    }

}
