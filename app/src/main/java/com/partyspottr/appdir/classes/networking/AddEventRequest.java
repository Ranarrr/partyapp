package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.classes.Requester;

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

public class AddEventRequest extends AsyncTask<Void, Void, Integer> {

    private JSONObject eventidanduser;
    private ProgressDialog progressDialog;

    public AddEventRequest(Activity activity, long eventid) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setOwnerActivity(activity);
        try {
            eventidanduser = new JSONObject();
            eventidanduser.put("user", new Gson().toJson(Requester.convertBrukerRequester(Bruker.get())));
            eventidanduser.put("eventId", eventid);
            eventidanduser.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Requesting.."); // TODO: translation
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("add_request", eventidanduser.toString()));
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

        return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        progressDialog.hide();
        if(integer == 1) {
            if(progressDialog.getOwnerActivity() != null) {
                ImageButton deltarBtn = progressDialog.getOwnerActivity().findViewById(R.id.details_delta_btn);
                deltarBtn.setEnabled(false);
                deltarBtn.setImageDrawable(progressDialog.getOwnerActivity().getResources().getDrawable(R.drawable.request_waiting));

                try {
                    Event eventtoChange = Bruker.get().getEventFromID(eventidanduser.getLong("eventId"));

                    if(eventtoChange != null) {
                        Requester requester = new Gson().fromJson(eventidanduser.getString("user"), Requester.type);
                        eventtoChange.addRequest(requester);
                        Bruker.get().setEventByID(eventtoChange.getEventId(), eventtoChange);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(progressDialog.getContext(), "Requested to join!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(progressDialog.getContext(), "Klarte ikke å sende forespørsel.", Toast.LENGTH_SHORT).show();
        }
    }
}
