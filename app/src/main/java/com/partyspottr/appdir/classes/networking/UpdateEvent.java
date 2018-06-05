package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.partyspottr.appdir.classes.Event;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UpdateEvent extends AsyncTask<Void, Void, Integer> {
    ProgressDialog progressDialog;
    private Event eventToUpdate;

    public UpdateEvent(Activity activity, Event event) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setOwnerActivity(activity);

        eventToUpdate = event;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("update_event", eventToUpdate.EventToJSON()));
            JSONObject json = new JSONParser().get_jsonobject(params);
            if(json != null) {
                if(json.getInt("success") == 1) {
                    return 1;
                } else {
                    return -1;
                }
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        progressDialog.hide();

        if(integer == 1) {
            Toast.makeText(progressDialog.getContext(), "Updated event!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(progressDialog.getContext(), "Failed to update event.", Toast.LENGTH_SHORT).show();
        }
    }
}
