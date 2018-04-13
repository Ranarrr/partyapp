package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Base64;

import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.classes.Bruker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UpdateChauffeur extends AsyncTask<Void, Void, Integer> {
    JSONObject info;

    public UpdateChauffeur(Activity activity) {
        info = new JSONObject();
        try {
            info.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
            info.put("chauffeur", Bruker.get().getChauffeur().ChauffeurJSONString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("update_chauffeur", info.toString()));
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
    protected void onPostExecute(Integer integer) {
        if(integer == 1) {

        } else {

        }
    }
}
