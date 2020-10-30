package com.example.mobilibrary.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilibrary.DatabaseController.DatabaseHelper;
import com.example.mobilibrary.R;

public class LogIn extends AppCompatActivity {
    private EditText inputEmail;
    private EditText inputPassword;

    private Button login;
    private TextView signup;
    private boolean emailVal = false;
    private boolean passwordVal = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        inputEmail = findViewById(R.id.email_editText);
        inputPassword = findViewById(R.id.password_editText);
        login = findViewById(R.id.login_button);
        signup = findViewById(R.id.signup_bar2);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogIn.this.startActivity(new Intent(LogIn.this, SignUp.class ));
            }
        });
        //Checks if user provides inputs
        String email = inputEmail.getText().toString().trim();
        if(email.isEmpty()){
            inputEmail.setError("Field cannot be empty");
        } else {
            inputEmail.setError(null);
            emailVal = true;
        }

         String password = inputPassword.getText().toString().trim();
        if(password.isEmpty()){
            inputPassword.setError("Field cannot be empty");
        } else {
            inputPassword.setError(null);
            passwordVal = true;
        }
        if ((emailVal)&&(passwordVal)){
            ProgressBar progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);
            DatabaseHelper databaseHelper = new DatabaseHelper(LogIn.this);
            databaseHelper.validateUser(email, password);
        }
    }
    @Override
    public void onBackPressed(){
        this.finish();
    }
}
