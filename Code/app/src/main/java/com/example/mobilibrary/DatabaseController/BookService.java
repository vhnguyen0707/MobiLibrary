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

public class BookService {
    private static final String TAG = "AddBookFragment";
    //Singleton class implementation
    private static BookService bookDb = null;
    private FirebaseFirestore db;
    private StorageReference storageReference;

    public static BookService getInstance(){
        if (BookService.bookDb == null)
            BookService.bookDb = new BookService();

        return BookService.bookDb;
    }

    private BookService(){
        db = FirebaseFirestore.getInstance();
    }

    public void addBook(final Context context, Book newBook){
        if (newBook.getFirestoreID()!= null)
            throw new IllegalArgumentException("This book is already added to the database");
        DocumentReference bookDoc = db.collection("Books").document(newBook.getTitle());
         Blob my_blob = Blob.fromBytes(newBook.getImage());
         Map<String, Object> data = new HashMap<>();
         data.put("ISBN", newBook.getISBN());
         data.put("Author", newBook.getAuthor());
         data.put("Status", newBook.getStatus());
         data.put("Owner", newBook.getOwner().getUsername());
         data.put("Image", my_blob);
         bookDoc.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
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

    /**
     * Attempts to delete book from database. Two different Toast messages depending on success or
     * failure of operation.
     * @param context current construct
     * @param deleteBook book object to delete
     */
    public void deleteBook(final Context context, Book deleteBook) {
        if (deleteBook.getFirestoreID() == null)
            throw new IllegalArgumentException("This book is not in database");
        // delete document
        db.collection("Books").document(deleteBook.getFirestoreID()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Successfully deleted book!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"Failed with: " +e.toString());
                        Toast.makeText(context, "Book not deleted!" , Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Attempts to edit a book in the database. Two different Toast messages depending on success or
     * failure of operation.
     * @param context current construct
     * @param editBook book object to edit
     */
    public void editBook(final Context context, Book editBook) {
        if (editBook.getFirestoreID() == null)
            throw new IllegalArgumentException("This book is not in database");

        // create hash map of fields that could be changed in editBook
        Blob my_blob = Blob.fromBytes(editBook.getImage());
        Map<String, Object> data = new HashMap<>();
        data.put("ISBN", editBook.getISBN());
        data.put("Author", editBook.getAuthor());
        data.put("Image", my_blob);
        data.put("Title", editBook.getTitle);

        // edit document
        db.collection("Books").document(editBook.getFirestoreID()).update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Successfully edited book!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"Failed with: " +e.toString());
                        Toast.makeText(context, "Book not edited!" , Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
