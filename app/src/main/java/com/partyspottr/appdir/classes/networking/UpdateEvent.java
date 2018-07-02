package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.partyspottr.appdir.classes.Event;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UpdateEvent extends AsyncTask<Void, Void, Boolean> {
    ProgressDialog progressDialog;
    private Event eventToUpdate;
    private Bitmap bitmap;

    public UpdateEvent(Activity activity, Event event, Bitmap bmp) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setOwnerActivity(activity);

        bitmap = bmp;
        eventToUpdate = event;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        JSONObject json = GoogleAPIRequest.makeHTTPReq(progressDialog.getContext(), eventToUpdate.getAddress(), eventToUpdate.getPostalcode());
        try {
            if(json.getString("status").equals("OK")) {
                eventToUpdate.ParseFromGoogleReq(json);

                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events").child(String.valueOf(eventToUpdate.getEventId()));
                ref.child("nameofevent").setValue(eventToUpdate.getNameofevent()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful())
                            return;

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
                        ref.child("postalcode").setValue(eventToUpdate.getPostalcode());
                        ref.child("desc").setValue(eventToUpdate.getDescription());
                        ref.child("showguestlist").setValue(eventToUpdate.isShowguestlist());
                        ref.child("showaddress").setValue(eventToUpdate.isShowaddress());
                        ref.child("hasimage").setValue(eventToUpdate.isHasimage());
                        ref.child("category").setValue(eventToUpdate.getCategory());
                    }
                });

                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean bool) {
        progressDialog.hide();

        if(bool) {
            Toast.makeText(progressDialog.getContext(), "Updated event!", Toast.LENGTH_SHORT).show();

            if(bitmap != null) {
                UploadImage uploadImage = new UploadImage(progressDialog, eventToUpdate, null, bitmap);
                uploadImage.execute();
            }
        } else
            Toast.makeText(progressDialog.getContext(), "Failed to update event.", Toast.LENGTH_SHORT).show();
    }
}
