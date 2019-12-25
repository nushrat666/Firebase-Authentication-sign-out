package com.example.firebaseauthentication_signupauser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText SignInEmailEditText, SignInPasswordEditText;
    private TextView signUpTextView;
    private Button SignInButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("Sign in Activity");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        progressBar=(ProgressBar) findViewById(R.id.progressbarId);
        SignInEmailEditText = (EditText) findViewById(R.id.signinemailedittextid);
        SignInPasswordEditText = (EditText) findViewById(R.id.signinpasswordedittextid);
        SignInButton = (Button) findViewById(R.id.signinButtonId);
        signUpTextView = (TextView) findViewById(R.id.SignUpTextViewId);

        signUpTextView.setOnClickListener(this);
        SignInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signinButtonId:
                UserLogin();
                break;

            case R.id.SignUpTextViewId:

                Intent intent = new Intent(getApplicationContext(), SignUpUser.class);
                startActivity(intent);
                break;
        }
    }

    private void UserLogin() {

        String email = SignInEmailEditText.getText().toString().trim();
        String password = SignInPasswordEditText.getText().toString().trim();

        //checking the validity of the email
        if (email.isEmpty()) {
            SignInEmailEditText.setError("please Enter an email address");
            SignInEmailEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            SignInEmailEditText.setError("please Enter a valid email address");
            SignInEmailEditText.requestFocus();
            return;
        }

        //checking the validity of the password
        if (password.isEmpty()) {
            SignInPasswordEditText.setError("Enter Password");
            SignInPasswordEditText.requestFocus();
            return;
        }
        if (password.length() < 6) {
            SignInPasswordEditText.setError("Minimum Length of a password should be 6");
            SignInPasswordEditText.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else {

                        Toast.makeText(getApplicationContext(), "Log In UnSuccessful!!", Toast.LENGTH_SHORT).show();
                    }
                }

        });
    }
}