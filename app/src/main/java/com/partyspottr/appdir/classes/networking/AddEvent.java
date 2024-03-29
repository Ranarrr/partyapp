package com.partyspottr.appdir.classes.networking;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Event;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ranarrr on 13-Feb-18.
 *
 * @author Ranarrr
 */

public class AddEvent extends AsyncTask<Void, Void, Integer> {
    private Event eventToUse;
    private ProgressDialog progressDialog;
    private Dialog dialog;
    private Bitmap bitmap;

    public AddEvent(Dialog dilog, Event event, Bitmap bmp) {
        dialog = dilog;
        eventToUse = event;
        eventToUse.setHasimage(bmp != null);
        bitmap = bmp;

        progressDialog = new ProgressDialog(dilog.getOwnerActivity(), R.style.mydatepickerdialog);
        progressDialog.setMessage("Adding event..");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        DatabaseReference eventidref = FirebaseDatabase.getInstance().getReference("events").child("eventidCounter");
        eventidref.setValue(Bruker.get().getEventIdCounter() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void task) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events");
                ref = ref.child(String.valueOf(Bruker.get().getEventIdCounter()));
                ref.child("nameofevent").setValue(eventToUse.getNameofevent());
                ref.child("hostStr").setValue(eventToUse.getHostStr());
                ref.child("address").setValue(eventToUse.getAddress());
                ref.child("town").setValue(eventToUse.getTown());
                ref.child("country").setValue(eventToUse.getCountry());
                ref.child("isprivate").setValue(eventToUse.isPrivateEvent());
                ref.child("longitude").setValue(eventToUse.getLongitude());
                ref.child("latitude").setValue(eventToUse.getLatitude());
                ref.child("datefrom").setValue(eventToUse.getDatefrom());
                ref.child("dateto").setValue(eventToUse.getDateto());
                ref.child("agerestriction").setValue(eventToUse.getAgerestriction());
                ref.child("participants").setValue(new Gson().toJson(eventToUse.getParticipants()));
                ref.child("requests").setValue(new Gson().toJson(eventToUse.getRequests()));
                ref.child("maxparticipants").setValue(eventToUse.getMaxparticipants());
                ref.child("desc").setValue(eventToUse.getDescription());
                ref.child("showguestlist").setValue(eventToUse.isShowguestlist());
                ref.child("showaddress").setValue(eventToUse.isShowaddress());
                ref.child("hasimage").setValue(eventToUse.isHasimage());
                ref.child("category").setValue(eventToUse.getCategory());

                Bruker.get().addToMyEvents(eventToUse);
                Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.event_lagt_til), Toast.LENGTH_SHORT).show();

                if(bitmap != null) {
                    UploadImage uploadImage = new UploadImage(progressDialog, eventToUse, bitmap);
                    uploadImage.execute();
                }

                if(dialog != null)
                    dialog.onBackPressed();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.tilkoblingsfeil), Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.hide();
            }
        });

        return null;
    }
}