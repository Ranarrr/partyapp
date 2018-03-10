package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.ui.ProfilActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Ranarrr on 23-Feb-18.
 */

public class AddRequest extends AsyncTask<Void, Void, Integer> {

    private JSONObject eventidanduser;

    private ProgressDialog progressDialog;

    public AddRequest(Activity activity, long eventid) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setOwnerActivity(activity);
        try {
            eventidanduser = new JSONObject();
            eventidanduser.put("user", new Gson().toJson(Bruker.get()));
            eventidanduser.put("eventId", eventid);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Requesting..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("add_request", eventidanduser.toString()));
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
        if(integer == 1) {
            if(progressDialog.getOwnerActivity() != null) {
                ImageButton deltarBtn = progressDialog.getOwnerActivity().findViewById(R.id.details_delta_btn);
                deltarBtn.setEnabled(false);
                deltarBtn.setImageDrawable(null);
            } else {
                Toast.makeText(progressDialog.getContext(), "Klarte ikke å sende forespørsel.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(progressDialog.getContext(), "Klarte ikke å sende forespørsel.", Toast.LENGTH_SHORT).show();
        }
    }
}
