package com.partyspottr.appdir.classes.networking;

import android.os.AsyncTask;
import android.util.Base64;

import com.partyspottr.appdir.BuildConfig;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SendToken extends AsyncTask<Void, Void, Void> {
    private JSONObject info;

    public SendToken(String token, String user) {
        info = new JSONObject();

        try {
            info.put("user", user);
            info.put("token", token);
            info.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("recieve_token", info.toString()));
        new JSONParser().get_jsonobject(params);

        return null;
    }
}
