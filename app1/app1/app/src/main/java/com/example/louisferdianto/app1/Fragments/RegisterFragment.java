package com.example.louisferdianto.app1.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.louisferdianto.app1.Activities.MainActivity;
import com.example.louisferdianto.app1.Activities.RegisterActivity;
import com.example.louisferdianto.app1.Models.User;
import com.example.louisferdianto.app1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private Button registerBtn;
    private EditText registerName, registerEmail,registerPassword,registerPhone;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    String userId;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        registerName = (EditText)v.findViewById(R.id.register_name);
        registerEmail = (EditText)v.findViewById(R.id.register_email);
        registerPassword = (EditText)v.findViewById(R.id.register_password);
        registerPhone = (EditText)v.findViewById(R.id.register_phone);
        registerBtn = (Button)v.findViewById(R.id.SignUpAccountBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!validateForm())
                {
                    return;
                }

                final String getemail = registerEmail.getText().toString();
                final String getpass = registerPassword.getText().toString();
                final String getname = registerName.getText().toString();
                final String getphone = registerPhone.getText().toString();
                mAuth.createUserWithEmailAndPassword(getemail, getpass)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (!task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Signed up Failed", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    FirebaseUser user = task.getResult().getUser();
                                    userId = user.getUid();
                                    User userSignUp = new User(getname,getemail,getpass,getphone,userId);
                                    firebaseFirestore.collection("Users").document(userId).set(userSignUp)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    mAuth.signOut();
                                                    Toast.makeText(getContext(), "Created Account", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("TAG", "Error writing document", e);
                                                }
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        return v;
    }
    private boolean validateForm()
    {
        boolean valid = true;
        String email = registerEmail.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            registerEmail.setError("Email Required");
            valid = false;
        }
        else
        {
            registerEmail.setError(null);
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                registerEmail.setError("Enter your email");
            }
            else
            {
                registerEmail.setError(null);
            }
        }

        String password = registerPassword.getText().toString();
        if(TextUtils.isEmpty(password))
        {
            registerPassword.setError("Password Required");
            valid = false;
        }
        else
        {
            if(password.length() > 5)
            {
                registerPassword.setError(null);
            }
            else
            {
                registerPassword.setError("Password length must be more than 5");
            }
        }

        String name = registerName.getText().toString();
        if(TextUtils.isEmpty(name))
        {
            registerName.setError("Name Required");
            valid = false;
        }
        else
        {
            registerName.setError(null);
        }

        String phone = registerPhone.getText().toString();
        if(TextUtils.isEmpty(phone))
        {
            registerPhone.setError("Phone Number Required");
        }
        else
        {
            registerPhone.setError(null);
        }
        return valid;
    }
}
