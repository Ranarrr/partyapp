package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.partyspottr.appdir.classes.Bruker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranarrr on 23-Feb-18.
 *
 * @author Ranarrr
 */

public class RemoveEvent extends AsyncTask<Void, Void, Integer> {
    private JSONObject eventidanduser;
    private ProgressDialog progressDialog;

    public RemoveEvent(Activity c, long eventid) {
        progressDialog = new ProgressDialog(c);
        try {
            eventidanduser = new JSONObject();
            eventidanduser.put("user", Bruker.get().getBrukernavn());
            eventidanduser.put("pass", Bruker.get().getPassord());
            eventidanduser.put("eventId", eventid);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void ... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("remove_event", eventidanduser.toString()));
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

        return -1;
    }

    @Override
    protected void onPostExecute(Integer o) {
        progressDialog.hide();
        if(o == 1) {
            Toast.makeText(progressDialog.getContext(), "Removed event!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(progressDialog.getContext(), "Failed to remove event.", Toast.LENGTH_SHORT).show();
        }
    }
}
