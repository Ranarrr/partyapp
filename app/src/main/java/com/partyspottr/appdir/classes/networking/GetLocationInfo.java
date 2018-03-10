package com.partyspottr.appdir.classes.networking;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.ui.ProfilActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/*
 * Created by Ranarrr on 22-Feb-18.
 */

public class GetLocationInfo extends AsyncTask<Void, Void, Integer> {

    private String addressToUse;
    private int PostalCode;
    private File bitmap;
    private Event eventToUse;
    ProgressDialog progressDialog;

    public GetLocationInfo(Context c, String address, int postal_code, Event event, File bmp) {
        progressDialog = new ProgressDialog(c);
        bitmap = bmp;
        addressToUse = address;
        PostalCode = postal_code;
        eventToUse = event;
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage(progressDialog.getContext().getResources().getString(R.string.legger_til_event));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        JSONObject json = GoogleAPIRequest.makeHTTPReq(progressDialog.getContext(), addressToUse, PostalCode);
        try {
            if(json.getString("status").equals("OK")) {
                eventToUse.ParseFromGoogleReq(json);
                return 1;
            } else {
                return -1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if(integer == 1) {
            AddEvent addEvent = new AddEvent(progressDialog, eventToUse, bitmap);
            addEvent.execute();
        } else {
            Toast.makeText(progressDialog.getContext(), "Failed to retreive location info for the event!", Toast.LENGTH_LONG).show();
        }
    }
}
