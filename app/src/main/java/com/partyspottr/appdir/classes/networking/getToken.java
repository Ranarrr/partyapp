package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.ChatPreview;
import com.partyspottr.appdir.classes.Chatter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class getToken extends AsyncTask<Void, Void, Boolean> {
    JSONObject info;
    ProgressDialog progressDialog;
    List<Chatter> chatters;
    String messag;
    String otheruser;

    public getToken(Activity activity, String user, List<Chatter> list, String message) {
        info = new JSONObject();
        chatters = list;
        messag = message;
        otheruser = user;

        progressDialog = new ProgressDialog(activity, R.style.mydatepickerdialog);
        progressDialog.setOwnerActivity(activity);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Starting chat..");

        try {
            info.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
            info.put("user", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("get_token", info.toString()));
            JSONObject json = new JSONParser().get_jsonobject(params);
            if(json != null) {
                if(json.getInt("success") == 1) {
                    for(Chatter chatter : chatters) {
                        if (chatter.getBrukernavn().equalsIgnoreCase(otheruser)) {
                            chatter.setToken(json.getString("token"));
                            return true;
                        }
                    }
                } else
                    return false;
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if(aBoolean && progressDialog.getOwnerActivity() != null)
            Bruker.get().startChat(progressDialog.getOwnerActivity(), new ChatPreview(messag, "", false, chatters), otheruser);
        else
            Toast.makeText(progressDialog.getContext(), "Failed to start a chat with " + otheruser, Toast.LENGTH_SHORT).show();

        progressDialog.hide();
    }
}
