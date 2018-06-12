package com.partyspottr.appdir.classes.networking;

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

public class UpdateUser extends AsyncTask<Void, Void, Void> {
    JSONObject info;

    public UpdateUser() {
        try {
            info = new JSONObject();
            info.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
            info.put("email", Bruker.get().getEmail());
            info.put("username", Bruker.get().getBrukernavn());
            info.put("passord", Bruker.get().getPassord());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("update_user", info.toString()));
        new JSONParser().get_jsonobject(params);

        return null;
    }
}
