package com.partyspottr.appdir.classes.networking;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Event;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranarrr on 26-Feb-18.
 *
 * @author Ranarrr
 */

public class UploadImage extends AsyncTask<Void, Void, Boolean> {

    private File bitmap;
    private Event eventPriv;
    private ProgressDialog progressDialog;

    public UploadImage(ProgressDialog pD, Event event, File bmp) {
        progressDialog = pD;
        eventPriv = event;
        bitmap = bmp;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("eventId", Long.toString(eventPriv.getEventId())));
            JSONObject json = new JSONParser().get_jsonobject("IMG", params, bitmap);
            if(json != null) {
                return json.getInt("success") == 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean bool) {
        if(bool) {
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.event_lagt_til), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(progressDialog.getContext(), "Failed to upload image.", Toast.LENGTH_SHORT).show();
        }
    }
}
