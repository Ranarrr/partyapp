package com.partyspottr.appdir.classes.networking;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.partyspottr.appdir.classes.Bruker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Ranarrr on 23-Feb-18.
 */

public class RemoveRequest extends AsyncTask<Void, Void, Integer> {

    JSONObject eventidanduser;
    ProgressDialog progressDialog;

    public RemoveRequest(Context c, long eventid) {
        try {
            eventidanduser = new JSONObject();
            eventidanduser.put("user", Bruker.get().getBrukernavn());
            eventidanduser.put("eventId", eventid);
        } catch(JSONException e) {
            e.printStackTrace();
        }

        progressDialog = new ProgressDialog(c);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("remove_request", eventidanduser.toString()));
            JSONObject json = new JSONParser().get_jsonobject("POST", params, null);
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

        return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {

    }
}
