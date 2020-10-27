package com.example.mobilibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilibrary.DatabaseController.DatabaseHelper;
import com.example.mobilibrary.DatabaseController.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.primitives.Chars;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity {

    private ImageButton editButton;
    private TextView usernameText;
    private TextView emailText;
    private TextView phoneText;
    private EditText editEmail;
    private EditText editPhone;
    private Button confirmButton;
    private Button cancelButton;
    private User profileUser;
    private User currentUser;
    private String profileUsername;
    private Context context;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Returning to the previous activity where the user came from
        final FloatingActionButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Get user information from database
        // profileUser from previous activity AND then by searching database?
        Intent intent = getIntent();
        profileUsername = (String) intent.getSerializableExtra("profile");
        databaseHelper = new DatabaseHelper(this);
        profileUser = databaseHelper.getUserProfile(profileUsername);
        currentUser = databaseHelper.getUser();

        // Set variables
        editButton = findViewById(R.id.edit_button);
        usernameText = findViewById(R.id.username_text_view);
        emailText = findViewById(R.id.email_text_view);
        phoneText = findViewById(R.id.phone_text_view);
        editEmail = findViewById(R.id.edit_email);
        editPhone = findViewById(R.id.edit_phone);
        confirmButton = findViewById(R.id.confirm_button);
        cancelButton = findViewById(R.id.cancel_button);
        context = getApplicationContext();

        // Set TextViews
        usernameText.setText(profileUser.getUsername());
        emailText.setText(profileUser.getEmail());
        phoneText.setText(profileUser.getPhoneNo());

        // Set visibility
        if (!profileUser.getUsername().equals(currentUser.getUsername())) {
            editButton.setVisibility(View.INVISIBLE);
        }

        emailText.setVisibility(View.INVISIBLE);
        phoneText.setVisibility(View.INVISIBLE);
        final List<View> toggleViews = new ArrayList<View>();
        toggleViews.add(editEmail);
        toggleViews.add(editPhone);
        toggleViews.add(cancelButton);
        toggleViews.add(confirmButton);
        toggleViews.add(emailText);
        toggleViews.add(phoneText);
        toggleVisibility(toggleViews);

        // User hitting edit button if they are viewing their own profile
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleViews.add(editButton);
                toggleVisibility(toggleViews);
                editEmail.setText(profileUser.getEmail());
                editPhone.setText(profileUser.getPhoneNo());

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleVisibility(toggleViews);
                    }
                });

                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!validateEmail(editEmail.getText().toString())) {
                             Toast toast = Toast.makeText(context, "Entered an invalid email!", Toast.LENGTH_SHORT);
                             toast.show();
                        } else if (!TextUtils.isEmpty(editPhone.getText().toString())){ // Phone number input & length already restricted by layout
                            // Update user with new email and/or phone in database
                            databaseHelper.updateUser(editEmail.getText().toString().trim(), editPhone.getText().toString().trim());
                            toggleVisibility(toggleViews);
                            Toast toast = Toast.makeText(context, "Profile saved!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });

            }
        });

    }

    /**
     * Switches visibility of a view from invisible to visible or vice versa.
     * @param views
     */
    public void toggleVisibility(List<View> views) {
        for (int i = 0; i < views.size(); i++) {
            if (views.get(i).getVisibility() == View.VISIBLE) {
                views.get(i).setVisibility(View.INVISIBLE);
            } else {
                views.get(i).setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Checks if the provided editText email is valid.
     * https://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext
     * @param target
     * @return true for valid email pattern, false otherwise
     */
    public boolean validateEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}