package com.example.crypticnotes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email_EditText, password_EditText;
    Button login_Btn;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_EditText = findViewById(R.id.email_editText);
        password_EditText = findViewById(R.id.password_editText);
        login_Btn = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.progress_bar);

        login_Btn.setOnClickListener((v) -> loginUser());

    }
    void loginUser(){
        String email = email_EditText.getText().toString();
        String password = password_EditText.getText().toString();

        boolean isValidated = validateData(email, password);
        if(!isValidated){
            return;
        }

        loginAccountInFirebase(email, password);
    }

    void loginAccountInFirebase(String email, String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    // login is the user entered the correct credentials
//                    if(firebaseAuth.getCurrentUser()){
//                        // go to main activity
//                    } else {
//                        //
//                    }
                } else {
                    // login failed
                    Utility.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
                }
            }
        });
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            login_Btn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            login_Btn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_EditText.setError("Email is invalid!");
            return false;
        }
        if (password.length() < 6 ){
            password_EditText.setError("Minimum Characters requirement not met: 6 Character");
            return false;
        }
        return true;
    }

}