package com.example.mobilibrary.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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


        signUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean failed = false;
                /*boolean boolUsername = false;
                boolean boolName = false;
                boolean boolEmail = false;
                boolean boolPhone = false;
                boolean boolPwd = false;

                 */

                //check Username
                if (inputUsername.getText().toString().trim().matches("^[a-zA-Z0-9._-]{3,}$")) {
                    inputUsername.setError(null);
                    //boolUsername = true;
                } else {
                    inputUsername.setError("Invalid input");
                    failed = true;
                }

                //check fullname
                if (!inputName.getText().toString().trim().isEmpty()) {
                    if (inputName.getText().toString().trim().matches("^([A-z\\'\\.-ᶜ]*(\\s))+[A-z\\'\\.-ᶜ]*$")) {
                        inputName.setError(null);
                        //boolName = true;
                    }
                } else {
                    inputName.setError("Invalid input");
                    failed=true;
                }



                //check Email
                if (inputEmail.getText().toString().trim().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    inputEmail.setError(null);
                    //boolEmail = true;
                } else {
                    inputEmail.setError("Invalid input");
                    failed = true;
                }

                //check Phone
                if (inputPhone.getText().toString().trim().matches("^[0-9-]{10}$")) {
                    inputPhone.setError(null);
                    //boolPhone = true;
                } else {
                    inputPhone.setError("Invalid input");
                    failed = true;
                }


                //check password
                if (!(inputPassword.getText().toString().trim().isEmpty())) {
                    inputPassword.setError(null);
                } else {
                    inputPassword.setError("Field cannot be empty");
                }

                //check confirm pwd
                if (inputConfirmPassword.getText().toString().trim().isEmpty()) {
                    inputConfirmPassword.setError("Field cannot be empty");
                    failed = true;
                }else if (!(inputPassword.getText().toString().trim().equals(inputConfirmPassword.getText().toString().trim()))){
                    inputConfirmPassword.setError("Passwords do not match");
                    failed=true;
                } else {
                    inputConfirmPassword.setError(null);
                    //boolPwd = true;
                }

                if(failed) return;

                //if ((boolUsername)&&(boolName)&&(boolEmail)&&(boolPhone)&&(boolPwd)){

                String username = inputUsername.getText().toString().trim();
                String fullname = inputName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String phoneNo = inputPhone.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                DatabaseHelper databaseHelper = new DatabaseHelper(SignUp.this);
                Toast.makeText(SignUp.this, "Loading", Toast.LENGTH_SHORT).show();
                databaseHelper.regCheck(username, password, fullname, email, phoneNo);
                }


        });

    }
}
/*
package com.example.mobilibrary.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilibrary.DatabaseController.DatabaseHelper;
import com.example.mobilibrary.R;


public class SignUp extends AppCompatActivity {
    private EditText inputUsername;
    private EditText inputName;
    private EditText inputEmail;
    private EditText inputPhone;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private Button signUp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //find layouts
        inputUsername = findViewById(R.id.edit_username);
        inputName = findViewById(R.id.edit_name);
        inputEmail = findViewById(R.id.edit_email);
        inputPhone = findViewById(R.id.edit_phoneNo);
        inputPassword = findViewById(R.id.edit_password);
        inputConfirmPassword = findViewById(R.id.edit_password2);
        signUp = findViewById(R.id.sign_up);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = inputUsername.getText().toString().trim();
                final String fullname = inputName.getText().toString().trim();
                final String email = inputEmail.getText().toString().trim();
                final String phoneNo = inputPhone.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();
                final String confirmpwd = inputConfirmPassword.getText().toString().trim();
                Log.d("", Boolean.toString(validateInputs(username, fullname, email, phoneNo, password, confirmpwd)));
                if (validateInputs(username, fullname, email, phoneNo, password, confirmpwd)) {
                    signUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            {
                                DatabaseHelper databaseHelper = new DatabaseHelper(SignUp.this);
                                databaseHelper.regCheck(username, password, fullname, email, phoneNo);
                            }
                        }
                    });
                }
            }
        });


    }
    private boolean validateInputs(String username, String fullname, String email, String phoneNo, String password, String confirmpwd){
        return(validateUsername(username) &
                validateFname(fullname) &
                validateEmail(email) &
                validatePhone(phoneNo) &
                validatePassword(password, confirmpwd));
    }

    private boolean validateUsername(String username){
            if (username.isEmpty()) {
                inputUsername.setError("Field cannot be empty");
                return false;
            } else {
                inputUsername.setError(null);
                return true;
            }

    }

    private boolean validateFname(String fullname){
        if (fullname.isEmpty()) {
            inputName.setError("Field cannot be empty");
            return false;
        } else {
            inputName.setError(null);
            return true;
        }
    }

    private boolean validateEmail(String email){
        if (email.isEmpty()) {
            inputEmail.setError("Field cannot be empty");
            return false;
        } else {
            inputEmail.setError(null);
            return true;
        }
    }

    private boolean validatePhone(String phoneNo){
        if (phoneNo.isEmpty()) {
            inputPhone.setError("Field cannot be empty");
            return false;
        } else if (phoneNo.length() < 6){
            inputPhone.setError("Please input at least 6 digits");
            return false;
        } else {
            inputPhone.setError(null);
            return true;
        }
    }

    private boolean validatePassword(String password, String confirmpwd){
        if (password.isEmpty()) {
            inputPassword.setError("Field cannot be empty");
            return false;
        } else if (confirmpwd.isEmpty()){
            inputConfirmPassword.setError("Field cannot be empty");
            return false;
        } else if (!password.equals(confirmpwd)){
            inputConfirmPassword.setError("Passwords do not match");
            return false;
        } else {
            inputName.setError(null);
            return true;
        }
    }
}  */