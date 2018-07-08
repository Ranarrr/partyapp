package com.partyspottr.appdir.classes.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotificationService extends IntentService {
    DatabaseReference notificationsref;
    ChildEventListener eventListener;

    String bruker;

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null) {
            if(intent.getAction() != null && intent.getAction().equalsIgnoreCase("QUIT")) {
                quitParsingNotifications();
            }

            if(intent.getAction() != null && intent.getAction().equalsIgnoreCase("START")) {
                bruker = intent.getStringExtra("user");
                startParsingNotifications();
            }
        }
    }

    private void quitParsingNotifications() {
        if(notificationsref != null)
            notificationsref.removeEventListener(eventListener);
    }

    private void startParsingNotifications() {
        notificationsref = FirebaseDatabase.getInstance().getReference("users").child(bruker);

        eventListener = notificationsref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey() != null) {
                    if(dataSnapshot.getKey().equalsIgnoreCase("notifications")) {
                        // TODO : maybe use Gson to save as json and then convert it to an array of notification object


                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
