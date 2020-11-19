package com.example.mobilibrary;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobilibrary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * Notifications fragment that is navigated to using the navigation bar. Shows the user's notifications they will receive when interacting
 * with other users. Clicking on a notification will lead to another activity depending on the kind of notification:
 * 1) Another user has requested your book -> lead to book details
 * 2) User has declined your request
 * 3) User has accepted your request -> lead to map with already-picked location to meet
 * 4) Location the user/borrower has selected to meet with the user/borrower has been sent
 * 5) The borrower is ready to return back User's book -> lead to map with already-picked location
 */
public class NotificationsFragment extends Fragment {

    RecyclerView notificationsRv;
    private ArrayList<ModelNotification> notificationsList;
    private AdapterNotification adapterNotification;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notifications, container, false);

        //init recycleview
        notificationsRv = v.findViewById(R.id.notificationsRV);

        getAllNotifications();

        return v;
    }

    private void getAllNotifications() {

        notificationsList = new ArrayList<>();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(getUsername()).collection("Notifications")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        notificationsList.clear();
                        for (final QueryDocumentSnapshot doc : value) {
                            String otherUser = Objects.requireNonNull(doc.get("otherUser")).toString();
                            String user = Objects.requireNonNull(doc.get("user")).toString();
                            String notification = Objects.requireNonNull(doc.get("notification")).toString();
                            String type = Objects.requireNonNull(doc.get("type")).toString();

                            ModelNotification model = new ModelNotification(otherUser, user, notification, type);
                            //add to list
                            notificationsList.add(model);
                        }
                        //adaptor
                        Collections.reverse(notificationsList); //Latest notification on top
                        adapterNotification = new AdapterNotification(getContext(), notificationsList);
                        //set to recycler view
                        notificationsRv.setAdapter(adapterNotification);

                    }
                });
    }

    private String getUsername(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userName = user.getDisplayName();
        return userName;
    }
}