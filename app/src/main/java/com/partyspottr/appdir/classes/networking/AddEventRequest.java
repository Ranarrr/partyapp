package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.classes.Requester;
import com.partyspottr.appdir.classes.Utilities;

import java.util.List;

/**
 * Created by Ranarrr on 23-Feb-18.
 *
 * @author Ranarrr
 */

public class AddEventRequest extends AsyncTask<Void, Void, Void> {
    private ProgressDialog progressDialog;
    private long eventId;

    public AddEventRequest(Activity activity, long eventid) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setOwnerActivity(activity);
        eventId = eventid;
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Requesting.."); // TODO: translation
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        final Event event = Bruker.get().getEventFromID(eventId);

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events").child(event.getHostStr() + "_" + event.getNameofevent()).child("requests");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                List<Requester> requesters = new Gson().fromJson(dataSnapshot.getValue(String.class), Utilities.listRequestsType);
                requesters.add(Requester.convertBrukerRequester(Bruker.get()));
                ref.child("requests").setValue(new Gson().toJson(requesters)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Activity a = progressDialog.getOwnerActivity();
                        if(a != null) {
                            AppCompatButton details_deltaforesprsler = a.findViewById(R.id.details_delta_btn);

                            details_deltaforesprsler.setEnabled(true);
                            details_deltaforesprsler.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Utilities.OnClickDetails(progressDialog.getOwnerActivity(), event);
                                }
                            });
                        }

                        Toast.makeText(progressDialog.getContext(), "Requested to join!", Toast.LENGTH_SHORT).show();
                        ref.onDisconnect();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(progressDialog.getContext(), "Failed to send request!", Toast.LENGTH_SHORT).show();
                        ref.onDisconnect();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.hide();
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        return null;
    }
}
