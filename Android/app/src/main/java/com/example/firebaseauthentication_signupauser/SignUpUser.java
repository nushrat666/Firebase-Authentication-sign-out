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

public class SignUpUser extends AppCompatActivity implements View.OnClickListener {
    private EditText SignUpEmailEditText, SignUpPasswordEditText;
    private TextView signInTextView;
    private Button SignUpButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user);

        this.setTitle("Sign up Activity");

        mAuth = FirebaseAuth.getInstance();
        progressBar=(ProgressBar) findViewById(R.id.progressbarId);
        SignUpEmailEditText = (EditText) findViewById(R.id.signUpemailedittextid);
        SignUpPasswordEditText = (EditText) findViewById(R.id.signUppasswordedittextid);
        SignUpButton = (Button) findViewById(R.id.signUpButtonId);
        signInTextView = (TextView) findViewById(R.id.SignInTextViewId);

        signInTextView.setOnClickListener(this);
        SignUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.signUpButtonId:
                userRgister();
                break;

            case R.id.SignInTextViewId:

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;

        }
    }

    private void userRgister() {
        String email = SignUpEmailEditText.getText().toString().trim();
        String password = SignUpPasswordEditText.getText().toString().trim();

        //checking the validity of the email
        if (email.isEmpty()) {
            SignUpEmailEditText.setError("please Enter an email address");
            SignUpEmailEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            SignUpEmailEditText.setError("please Enter a valid email address");
            SignUpEmailEditText.requestFocus();
            return;
        }

        //checking the validity of the password
        if (password.isEmpty()) {
            SignUpPasswordEditText.setError("Enter Password");
            SignUpPasswordEditText.requestFocus();
            return;
        }
        if (password.length() < 6) {
            SignUpPasswordEditText.setError("Minimum Length of a password should be 6");
            SignUpPasswordEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "User is already Registered", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
