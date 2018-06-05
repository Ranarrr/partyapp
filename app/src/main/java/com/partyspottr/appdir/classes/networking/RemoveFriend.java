package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.classes.Bruker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RemoveFriend extends AsyncTask<Void, Void, Integer> {
    JSONObject info;
    ProgressDialog progressDialog;
    private String error_msg;

    RemoveFriend(Activity activity, String friend) {
        progressDialog = new ProgressDialog(activity);
        try {
            info = new JSONObject();
            info.put("friend", friend);
            info.put("user", Bruker.get().getBrukernavn());
            info.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("remove_friend", info.toString()));
            JSONObject json = new JSONParser().get_jsonobject(params);
            if(json != null) {
                if(json.getInt("success") == 1) {
                    return 1;
                } else {
                    error_msg = json.getString("error_msg");
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
            Toast.makeText(progressDialog.getContext(), "Removed friend!", Toast.LENGTH_SHORT).show();
        } else {
            if(error_msg.equals("Could not find friend in friendlist."))
                Toast.makeText(progressDialog.getContext(), "This user is not your friend.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(progressDialog.getContext(), "Failed to remove friend.", Toast.LENGTH_SHORT).show();
        }
    }
}
