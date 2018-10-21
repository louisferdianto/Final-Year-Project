package com.example.louisferdianto.app1.Activities;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.louisferdianto.app1.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    RelativeLayout rellay1, rellay2;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };
    private EditText uemail, upassword;
    private Button loginemail;
    private TextView registerTextView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);

        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash

        final List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());

        uemail = (EditText)findViewById(R.id.user_email_login);
        upassword = (EditText)findViewById(R.id.user_password_login);
        registerTextView = (TextView)findViewById(R.id.register_textView);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        loginemail = (Button)findViewById(R.id.login_email_button);
        loginemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(uemail.getText().toString(),upassword.getText().toString());
            }
        });
        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(user != null)
                {
                    Log.d("TAG","onAuthStateChanged:signed_in"+user.getUid());
                }
                else
                {
                    Log.d("TAG","onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener !=null)
        {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    private void signIn(String email, String password)
    {
        if(!validateForm())
        {
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
                    else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.getException());
                    Toast.makeText(getApplicationContext(), "Login failed.", Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                }

                // ...
            }
    });
    }
    private void updateUI(FirebaseUser user)
    {
        Intent intent = new Intent(getApplicationContext(), BottomNavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private boolean validateForm()
    {
        boolean valid = true;
        String email = uemail.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            uemail.setError("Email Required");
            valid = false;
        }
        else
        {
            uemail.setError(null);
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                uemail.setError("Enter your email");
            }
            else
            {
                uemail.setError(null);
            }
        }

        String password = upassword.getText().toString();
        if(TextUtils.isEmpty(password))
        {
            upassword.setError("Password Required");
            valid = false;
        }
        else
        {
            if(password.length() > 5)
            {
                upassword.setError(null);
            }
            else
            {
                upassword.setError("Password length must be more than 5");
            }
        }
        return valid;
    }

    public static void startSignOut(final FragmentActivity fragmentActivity)
    {
        AuthUI.getInstance()
                .signOut(fragmentActivity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(fragmentActivity,"Signed Out", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                Intent intent = new Intent(this, BottomNavigationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                // Sign in failed, check response for error code
                Toast.makeText(this,"Invalid user", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
