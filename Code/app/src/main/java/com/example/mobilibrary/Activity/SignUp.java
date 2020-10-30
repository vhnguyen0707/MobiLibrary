package com.example.mobilibrary.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilibrary.DatabaseController.DatabaseHelper;
import com.example.mobilibrary.R;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    private EditText inputUsername;
    private EditText inputName;
    private EditText inputEmail;
    private EditText inputPhone;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private ImageButton back;
    private Button signUp;

    private boolean boolUsername = false;
    private boolean boolName = false;
    private boolean boolEmail = false;
    private boolean boolPhone = false;
    private boolean boolPwd = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputUsername = findViewById(R.id.edit_username);
        inputName = findViewById(R.id.edit_name);
        inputEmail = findViewById(R.id.edit_email);
        inputPhone = findViewById(R.id.edit_phoneNo);
        inputPassword = findViewById(R.id.edit_password);
        inputConfirmPassword = findViewById(R.id.edit_password2);
        signUp = findViewById(R.id.sign_up);
        back = findViewById(R.id.back_button);

//go back to log in screen if the software back button is pressed
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, LogIn.class));
            }
        });
/*-----------------------------------------------------------------------------------------
 * Instantiate TextWatcher to monitor inputs
 * Boolean values are set to false for empty fields at start
 * Checks if inputs are in correct format
 * If passes, set Error to null and boolean values to true
 ------------------------------------------------------------------------------------------*/
        final String username = inputUsername.getText().toString().trim();
        inputUsername.setError("Field cannot be empty");
        inputUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (username.matches("^[a-zA-Z0-9._-]{3,}$")) {
                    inputUsername.setError(null);
                    boolUsername = true;
                } else {
                    inputUsername.setError("Invalid input");
                }
            }
        });


        final String fullname = inputName.getText().toString().trim();
        inputName.setError("Field cannot be empty");
        inputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!fullname.isEmpty()) {
                    if (fullname.matches("^([A-z\\'\\.-ᶜ]*(\\s))+[A-z\\'\\.-ᶜ]*$")) {
                        inputName.setError(null);
                        boolName = true;
                    }
                } else {
                    inputName.setError("Invalid input");
                }
            }
        });

        inputEmail.setError("Field cannot be empty");
        inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (inputEmail.getText().toString().trim().matches("^([A-z\\'\\.-ᶜ]*(\\s))+[A-z\\'\\.-ᶜ]*$")) {
                    inputEmail.setError(null);
                    boolEmail = true;
                } else {
                    inputEmail.setError("Invalid input");
                }
            }
        });

        inputPhone.setError("Field cannot be empty");
        inputPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (inputPhone.getText().toString().trim().matches("^[0-9-]{10}$")) {
                    inputPhone.setError(null);
                    boolPhone = true;
                } else {
                    inputPhone.setError("Invalid input");
                }
            }
        });

        inputPassword.setError("Field cannot be empty");
        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!(inputPassword.getText().toString().trim().isEmpty())) {
                    inputPassword.setError(null);
                } else {
                    inputPassword.setError("Field cannot be empty");
                }
            }
        });

        inputConfirmPassword.setError("Field cannot be empty");
        inputConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (inputConfirmPassword.getText().toString().trim().isEmpty()) {
                    inputConfirmPassword.setError("Field cannot be empty");
                }else if (!(inputPassword.getText().toString().trim().equals(inputConfirmPassword.getText().toString().trim()))){
                    inputConfirmPassword.setError("Passwords do not match");
                } else {
                    inputConfirmPassword.setError(null);
                    boolPwd = true;
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((boolUsername)&&(boolName)&&(boolEmail)&&(boolPhone)&&(boolPwd)){
                    String email = inputEmail.getText().toString().trim();
                    String phoneNo = inputPhone.getText().toString().trim();
                    String password = inputPassword.getText().toString();
                    DatabaseHelper databaseHelper = new DatabaseHelper(SignUp.this);
                    databaseHelper.regCheck(username, password, fullname, email, phoneNo);
                }

            }
        });

    }
}



