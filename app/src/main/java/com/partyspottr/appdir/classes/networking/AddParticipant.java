package com.partyspottr.appdir.classes.networking;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Participant;
import com.partyspottr.appdir.enums.EventStilling;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Ranarrr on 15-Feb-18.
 */

public class AddParticipant extends AsyncTask<Void, Void, Integer> {

    private JSONObject eventidanduser;
    private ProgressDialog progressDialog;

    public AddParticipant(Context c, long eventid) {
        progressDialog = new ProgressDialog(c);
        try{
            eventidanduser = new JSONObject();
            if(Bruker.get().isPremium()) {
                eventidanduser.put("user", new Gson().toJson(Participant.convertBrukerParticipant(Bruker.get(), EventStilling.PREMIUM)));
            } else {
                eventidanduser.put("user", new Gson().toJson(Participant.convertBrukerParticipant(Bruker.get(), EventStilling.GJEST)));
            }

            eventidanduser.put("eventId", eventid);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Participating..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("add_participant", eventidanduser.toString()));
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
        progressDialog.hide();
        if(integer == 1) {
            Toast.makeText(progressDialog.getContext(), "Participated!", Toast.LENGTH_SHORT).show();

        } else if (integer == 0){
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.tilkoblingsfeil), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(progressDialog.getContext(), "Failed to participate.", Toast.LENGTH_SHORT).show();
        }
    }
}
