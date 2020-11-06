package com.example.mobilibrary.DatabaseController;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mobilibrary.Book;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

/**
 * This class interacts with the database to get books on the cloud
 * and do all the database related tasks for the books

 */
public class BookService {
    private static final String TAG = "AddBookFragment";
    //Singleton class implementation
    private static BookService bookDb = null;
    private FirebaseFirestore db;
    private StorageReference storageReference;

    /**
     * This methods gets the instance of BookService class. Creates one if it does not exist
     * @return the instance of BookService class
     */
    public static BookService getInstance(){
        if (BookService.bookDb == null)
            BookService.bookDb = new BookService();

        return BookService.bookDb;
    }

    /**
     * Singleton class implementation. This constructor instantiating the only instance of BookService.
     */
    private BookService(){
        db = FirebaseFirestore.getInstance();
    }

    /**
     * This method attempts to add a new book to the database. Two different toast messages one for success and
     * one for failure
     * @param context the current construct
     * @param newBook new Book object
     */

    public void addBook(final Context context, final Book newBook){
        // Checks if the book is already added to database
        if (newBook.getFirestoreID()!= null)
            throw new IllegalArgumentException("This book is already added to the database");
         Blob my_blob = Blob.fromBytes(newBook.getImage());
         Map<String, Object> data = new HashMap<>();
         data.put("Title", newBook.getTitle());
         data.put("ISBN", newBook.getISBN());
         data.put("Author", newBook.getAuthor());
         data.put("Status", newBook.getStatus());
         data.put("Owner", newBook.getOwner().getUsername());
         data.put("Image", my_blob);
        db.collection("Books").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
             @Override
             public void onSuccess(DocumentReference documentReference) {
                 newBook.setFirestoreID(documentReference.getId());
                 Toast.makeText(context, "Successfully added book!", Toast.LENGTH_SHORT).show();
             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Log.e(TAG,"Failed with: " +e.toString());
                 Toast.makeText(context, "Book not added!" , Toast.LENGTH_SHORT).show();
             }
         });
    }

    /**
     * This method checks if the image successfully uploaded to FirebaseStorage
     * @param title Name of the book
     * @param imageUri The URI of the image to save
     * @param successListener A SuccessListener of type Void. Called if the tasks succeeded
     * @param failureListener A FailureListener. Called when the task failed
     */

    public void uploadImage(String title, Uri imageUri, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        StorageReference fileRef = storageReference.child(title);
        fileRef.putFile(imageUri)
                .continueWith(new Continuation<UploadTask.TaskSnapshot, Void>() {
                    @Override
                    public Void then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        task.getResult();
                        return null;
                    }
                })
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);

    }



}
