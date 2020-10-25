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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.concurrent.Executor;

public class DatabaseHelper {
    private static final String TAG = "DatabaseHelper";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Context context;

    public DatabaseHelper(Context context) {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        this.context = context;
    }

    /**
     * https://stackoverflow.com/questions/52861391/firestore-checking-if-username-already-exists
     **/

    public void regCheck(final String username, final String password, final String fullname, final String email, final String phoneNo) {
        db.collection("User")
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
     * https://stackoverflow.com/questions/46795817/how-to-efficiently-add-items-to-collection-with-firebase-firestore
     * https://stackoverflow.com/questions/50087616/firebase-transaction-read-and-update-multiple-documents
     *
     * @param username
     * @param password
     * @param name
     * @param email
     * @param phoneNo
     */
    private void registerUser(final String username, final String password, final String name, final String email, final String phoneNo) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "New user added", Toast.LENGTH_SHORT).show();
                            //HashMap stores the user data in form of key-value pairs to send to Firestore
                            HashMap<String, Object> user = new HashMap<>();
                            user.put("FullName", name);
                            user.put("Email", email);
                            user.put("Phone", phoneNo);

                            DocumentReference userRef = db.collection("Users").document(username);
                            userRef.set(user)
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
                    }
                });
    }

    public void validateUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Authentication succeeded.", Toast.LENGTH_SHORT).show();
                            //log in to homepage
                            context.startActivity(new Intent(context, HomePage.class));
                        }else {
                            Toast.makeText(context, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            //go to log in screen again to prompt a new attempt
                            context.startActivity(new Intent(context, LogIn.class));
                        }
                    }
                });
    }


}
