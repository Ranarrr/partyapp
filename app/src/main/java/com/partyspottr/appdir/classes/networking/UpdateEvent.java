package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
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
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Event;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ranarrr on 03-Jun-18.
 *
 * @author Ranarrr
 */

public class UpdateEvent extends AsyncTask<Void, Void, Void> {
    ProgressDialog progressDialog;
    private Event eventToUpdate;
    private Bitmap bitmap;

    public UpdateEvent(Activity activity, Event event, Bitmap bmp) {
        progressDialog = new ProgressDialog(activity, R.style.mydatepickerdialog);
        progressDialog.setOwnerActivity(activity);

        bitmap = bmp;
        eventToUpdate = event;
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Updating event..");
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events").child(String.valueOf(eventToUpdate.getEventId()));
        ref.child("nameofevent").setValue(eventToUpdate.getNameofevent()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ref.child("address").setValue(eventToUpdate.getAddress());
                ref.child("town").setValue(eventToUpdate.getTown());
                ref.child("country").setValue(eventToUpdate.getCountry());
                ref.child("isprivate").setValue(eventToUpdate.isPrivateEvent());
                ref.child("longitude").setValue(eventToUpdate.getLongitude());
                ref.child("latitude").setValue(eventToUpdate.getLatitude());
                ref.child("datefrom").setValue(eventToUpdate.getDatefrom());
                ref.child("dateto").setValue(eventToUpdate.getDateto());
                ref.child("agerestriction").setValue(eventToUpdate.getAgerestriction());
                ref.child("maxparticipants").setValue(eventToUpdate.getMaxparticipants());
                ref.child("desc").setValue(eventToUpdate.getDescription());
                ref.child("showguestlist").setValue(eventToUpdate.isShowguestlist());
                ref.child("showaddress").setValue(eventToUpdate.isShowaddress());
                ref.child("hasimage").setValue(eventToUpdate.isHasimage());
                ref.child("category").setValue(eventToUpdate.getCategory());

                Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.updated_event), Toast.LENGTH_SHORT).show();

                if(bitmap != null) {
                    UploadImage uploadImage = new UploadImage(progressDialog, eventToUpdate, bitmap);
                    uploadImage.execute();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.failed_to_update), Toast.LENGTH_SHORT).show();
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
