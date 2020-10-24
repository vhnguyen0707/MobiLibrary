package com.example.mobilibrary.DatabaseController;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseHelper {
    private static final String TAG = "DatabaseHelper";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Context context;

    public DatabaseHelper(Context context){
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        this.context = context;
    }

    public void checkExist(final String username){
        db.collection("User")
                .document(username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()){
                                Toast.makeText(context, "Username already exists. Please try again!", Toast.LENGTH_SHORT ).show();
                                return;
                            }
                        }
                        else{
                            registerUser(userName, password, name, emailAdress, phoneNo);
                        }
                    }
                });
    }

    private void registerUser(String username, String password, String name, String emailAddress, string phonerNo){

    }


}
