package com.partyspottr.appdir.classes.networking;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranarrr on 31-Jan-18.
 *
 * @author Ranarrr
 */

public class getUser extends AsyncTask<Void, Void, Integer> {

    private ProgressDialog progressDialog;
    private JSONObject info;

    public getUser(Context c) {
        progressDialog = new ProgressDialog(c);
        try {
            info = new JSONObject();
            info.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
            info.put("username", Bruker.get().getBrukernavn());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try{
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("get_user", info.toString()));
            JSONObject json = new JSONParser().get_jsonobject("POST", params, null);
            if(json != null) {
                if(json.getInt("success") == 1) {
                    Bruker.get().JSONToBruker(json);
                    return 1;
                } else {
                    return 0;
                }
            }
        } catch(JSONException e) {
            e.printStackTrace();
            return 0;
        }

        return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if(integer == 0) {
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.hente_bruker_mislykkes), Toast.LENGTH_SHORT).show();
        }
    }
}
