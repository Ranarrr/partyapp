package com.partyspottr.appdir.classes.networking;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Event;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranarrr on 13-Feb-18.
 *
 * @author Ranarrr
 */

public class AddEvent extends AsyncTask<Void, Void, Integer> {
    private Event eventToUse;
    private ProgressDialog progressDialog;
    private Dialog dialog;

    AddEvent(Dialog dilog, ProgressDialog pD, Event event, File bmp) {
        progressDialog = pD;
        dialog = dilog;
        eventToUse = event;
        eventToUse.setHasimage(bmp != null);
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events").child(eventToUse.getHostStr() + "_" + eventToUse.getNameofevent());
        ref.child("eventId").push();
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
        ref.child("postalcode").setValue(eventToUse.getPostalcode());
        ref.child("desc").setValue(eventToUse.getDescription());
        ref.child("showguestlist").setValue(eventToUse.isShowguestlist());
        ref.child("showaddress").setValue(eventToUse.isShowaddress());
        ref.child("hasimage").setValue(eventToUse.isHasimage());

        return 1;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if(integer == 1) {
            Bruker.get().addToMyEvents(eventToUse);
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.event_lagt_til), Toast.LENGTH_SHORT).show();
            progressDialog.hide();

            if(dialog != null) {
                dialog.onBackPressed();
            }

        } else if(integer == 0) {
            progressDialog.hide();
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.tilkoblingsfeil), Toast.LENGTH_SHORT).show();
        } else if(integer == -1) {
            progressDialog.hide();
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.legge_til_event_mislyktes), Toast.LENGTH_SHORT).show();
        }
    }
}