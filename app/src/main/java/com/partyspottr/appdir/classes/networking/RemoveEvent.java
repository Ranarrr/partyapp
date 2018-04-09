package com.partyspottr.appdir.classes.networking;

import android.content.Context;
import android.os.AsyncTask;

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

public class RemoveEvent extends AsyncTask {

    private JSONObject eventidanduser;

    public RemoveEvent(Context c, long eventid) {
        try {
            eventidanduser = new JSONObject();
            eventidanduser.put("user", Bruker.get().getBrukernavn());
            eventidanduser.put("eventId", eventid);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
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

        return 0;
    }

    @Override
    protected void onPostExecute(Object o) {

    }
}
