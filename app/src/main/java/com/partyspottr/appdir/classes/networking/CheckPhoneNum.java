package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.ui.registerui.Register3Activity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranarrr on 23-Mar-18.
 *
 * @author Ranarrr
 */

public class CheckPhoneNum extends AsyncTask<Void, Void, Integer> {
    private ProgressDialog progressDialog;
    private JSONObject info;


    public CheckPhoneNum(Activity activity, String phone) {
        try {
            info = new JSONObject();
            info.put("phone", phone);
            info.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog = new ProgressDialog(activity);
        progressDialog.setOwnerActivity(activity);
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Checking phone number.."); // TODO : translation
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("check_phone", info.toString()));
            JSONObject json = new JSONParser().get_jsonobject(params);
            if(json != null) {
                if(json.getInt("taken") == 1) {
                    return -1;
                } else {
                    return 1;
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
            if(progressDialog.getOwnerActivity() != null) {
                Intent intent = new Intent(progressDialog.getOwnerActivity(), Register3Activity.class);
                progressDialog.getOwnerActivity().startActivity(intent);
            }
        } else {
            Toast.makeText(progressDialog.getContext(), "This phone number is already taken.", Toast.LENGTH_SHORT).show(); // TODO : Translation
        }
    }
}
