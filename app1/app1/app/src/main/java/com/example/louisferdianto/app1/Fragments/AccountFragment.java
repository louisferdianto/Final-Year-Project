package com.example.louisferdianto.app1.Fragments;


import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.louisferdianto.app1.Activities.MainActivity;
import com.example.louisferdianto.app1.Data.DataRepository;
import com.example.louisferdianto.app1.Models.Upload;
import com.example.louisferdianto.app1.Models.User;
import com.example.louisferdianto.app1.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private EditText name;
    private TextView email;
    private EditText phone;
    private EditText password;
    private CircleImageView userPhoto;
    private Button logoutBtn, editBtn;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    DataRepository dataRepository = new DataRepository();
    private StorageReference storageReference;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;
    Upload upload;
    String photo;
    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        name = (EditText) v.findViewById(R.id.userName);
        email = (TextView) v.findViewById(R.id.userEmail);
        phone = (EditText) v.findViewById(R.id.userPhone);
        password = (EditText) v.findViewById(R.id.userPassword);
        storageReference = FirebaseStorage.getInstance().getReference("uploadImages");

        userPhoto = (CircleImageView)v.findViewById(R.id.userImage);
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChoser();
            }
        });
        if(user!=null) {
            dataRepository.getUserAccountDetails(user.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot)
                {
                    User userAccount = documentSnapshot.toObject(User.class);
                    name.setText(userAccount.getUserName());
                    email.setText(userAccount.getUserEmail());
                    phone.setText(userAccount.getUserPhone());
                    password.setText(userAccount.getUserPassword());
                    Glide.with(userPhoto.getContext())
                            .load(userAccount.getUserPhoto())
                            .into(userPhoto);
                }
            });
        }
        else
        {
            goLogInScreen();
        }
        logoutBtn = (Button)v.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(this);
        editBtn = (Button)v.findViewById(R.id.editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEditProfile();
            }
        });

        return v;
    }

    private void saveEditProfile()
    {
        String editName = name.getText().toString();
        String editPassword = password.getText().toString();
        String editPhone = phone.getText().toString();
        User editUserAccount = new User();
        editUserAccount.setUserName(editName);
        editUserAccount.setUserPhone(editPhone);
        editUserAccount.setUserPassword(editPassword);

        dataRepository.getUserAccountDetails(user.getUid());
        dataRepository.docRef.update(
                "userName",editName, "userPhone",editPhone ,"userPassword",editPassword
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "DocumentSnapshot successfully updated!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error updating document", e);
                    }
                });

        user.updatePassword(editPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "User password updated.");
                        }
                    }
                });

        if(uploadTask != null && uploadTask.isInProgress())
        {
            Toast.makeText(getContext(),"Upload in Progress",Toast.LENGTH_SHORT).show();
        }
        else {
            if (imageUri != null)
            {
                StorageReference fileReference = storageReference.child(getFileExtension(imageUri));
                uploadTask = fileReference.putFile(imageUri);
                Task<Uri> urlTask = uploadTask.continueWithTask((Continuation<UploadTask.TaskSnapshot, Task<Uri>>) task -> {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }

                    // Continue with the task to get the download URL

                    return fileReference.getDownloadUrl();

                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            upload = new Upload(name.getText().toString().trim(),downloadUri.toString());
                            photo = upload.getImageUrl();
                            Log.d("TAG", photo);

                            dataRepository.getUserAccountDetails(user.getUid());
                            dataRepository.docRef.update(
                                    "userName",editName, "userPhone",editPhone, "userPhoto",photo ,"userPassword",editPassword
                            ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "DocumentSnapshot successfully updated!");
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("TAG", "Error updating document", e);
                                        }
                                    });

                            user.updatePassword(editPassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("TAG", "User password updated.");
                                            }
                                        }
                                    });
                        } else {
                            // Handle failures
                            // ...


                        }
                    }
                });
            }
        }
    }

    private void openFileChoser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,PICK_IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri)
    {
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case PICK_IMAGE_REQUEST:
                    imageUri = data.getData();
                    Picasso.with(getContext()).load(imageUri).into(userPhoto);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void goLogInScreen() {
        Intent intent = new Intent(AccountFragment.this.getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        int view = v.getId();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment newFragment;

        switch (view)
        {
            case R.id.logoutBtn:
                MainActivity.startSignOut(AccountFragment.this.getActivity());
                goLogInScreen();
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}