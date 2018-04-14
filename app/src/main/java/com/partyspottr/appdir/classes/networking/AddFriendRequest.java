package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import com.google.gson.Gson;
import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Friend;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddFriendRequest extends AsyncTask<Void, Void, Integer> {
    JSONObject info;
    ProgressDialog progressDialog;

    public AddFriendRequest(Activity activity, String bruker) {
        progressDialog = new ProgressDialog(activity);
        info = new JSONObject();
        try {
            info.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
            info.put("brukernavn", bruker);
            info.put("friend", new Gson().toJson(Friend.BrukerToFriend(Bruker.get())));
        } catch (JSONException e) {
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
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("add_friend_request", info.toString()));
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
        progressDialog.hide();
        if(integer == 1) {
            Toast.makeText(progressDialog.getContext(), "Sent friend request!", Toast.LENGTH_SHORT).show(); // TODO : Fix translation
        } else {
            Toast.makeText(progressDialog.getContext(), "Failed to send friend request! Please try again later.", Toast.LENGTH_LONG).show();
        }
    }
}
