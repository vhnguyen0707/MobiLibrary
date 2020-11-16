package com.example.mobilibrary.DatabaseController;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestService {
    //Singleton class implementation
    private static RequestService requestDb = null;
    private FirebaseFirestore db;

    public static RequestService getInstance(){
        if (RequestService.requestDb == null)
            RequestService.requestDb = new RequestService();
        return RequestService.requestDb;
    }

    private RequestService(){
        db = FirebaseFirestore.getInstance();
    }

    public Request getRequest(DocumentSnapshot documentSnapshot){
        String requester = documentSnapshot.getString("requester");
        String bookID = documentSnapshot.getString("bookID");
        return new Request(documentSnapshot.getId(), requester, bookID);
    }

    public Task<Void> acceptRequest(Request request){
        WriteBatch batch = db.batch();

        DocumentReference requestDoc = db.collection("Requests")
                .document(request.getID());

        DocumentReference bookDoc = db.collection("Books")
                .document(request.getBookID());

        DocumentReference userDoc = db.collection("Users")
                .document(request.getRequester());

        Map<String, Object> newData = new HashMap<>();
        newData.put("Borrowing", FieldValue.arrayUnion(request.getBookID()));
        batch.update(userDoc, newData);
        batch.delete(requestDoc);
        batch.update(bookDoc, "status", "Borrowed");
        return batch.commit();
    }

    public Task<Void> declineOthers(List<String> IDs){
        WriteBatch batch = db.batch();
        for (String requestID: IDs) {
            batch.delete(db.collection("Requests").document(requestID));
        }
        return batch.commit();
    }

    public Task<Void> decline(String requestID){
        return db.collection("Requests").document(requestID).delete();
    }
}




