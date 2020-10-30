package com.example.mobilibrary.DatabaseController;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mobilibrary.Activity.LogIn;
import com.example.mobilibrary.HomePage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper {
    private static final String TAG = "DatabaseHelper";
    static final String UsersCol = "Users";
    private FirebaseFirestore db;
    private FirebaseUser user;
    private Context context;

    public DatabaseHelper(Context context) {
        db = FirebaseFirestore.getInstance();
        this.context = context;
    }


    public void regCheck(final String username, final String password, final String fullname, final String email, final String phoneNo) {
        db.collection(UsersCol)
                .document(username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()) {
                                Toast.makeText(context, "Username already exists. Please try again!", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                registerUser(username, password, fullname, email, phoneNo);
                            }

                        }
                    }
                });
    }

    /**
     *
     * @param username
     * @param password
     * @param name
     * @param email
     * @param phoneNo
     */
    private void registerUser(final String username, final String password, final String name, final String email, final String phoneNo) {
        //HashMap stores the user data in form of key-value pairs to send to Firestore
        //User data
        Map<String, Object> userData = new HashMap<>();
        userData.put("Username", username);
        userData.put("Fullname", name);
        userData.put("Email", email);
        userData.put("Phone", phoneNo);
        userData.put("Credential", password);
        //Create user data doc
        DocumentReference userRef = db.collection(UsersCol).document(username);
        userRef.set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User Profile is created for " + username);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "User Profile could not be added!" + e.toString());
                 }
                });
        //go back to login screen so the user can log in
        context.startActivity(new Intent(context, LogIn.class));
    }


    public void validateUser(String username, final String password){
        db.collection(UsersCol)
                .document(username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            if (password.equals(doc.get("Credential"))){
                                Toast.makeText(context, "Authentication succeeded.", Toast.LENGTH_SHORT).show();
                                context.startActivity(new Intent(context, HomePage.class));
                            } else {
                                Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                //go to login screen again to prompt a new attempt
                                context.startActivity(new Intent(context, LogIn.class));
                            }
                        }else{
                            Log.d(TAG, "Failed with", task.getException());
                        }
                    }
                });

    }


}
