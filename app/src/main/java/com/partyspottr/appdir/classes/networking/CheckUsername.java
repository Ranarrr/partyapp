package com.partyspottr.appdir.classes.networking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.ui.registerui.Register4Activity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranarrr on 27-Jan-18.
 * NETWORKING ASYNCHRONOUSLY
 *
 * @author Ranarrr
 */

public class CheckUsername extends AsyncTask<Void, Void, Integer> {
    private JSONObject info;
    private ProgressDialog progressDialog;

    public CheckUsername(String usrname, Context c) {
        try {
            info = new JSONObject();
            info.put("username", usrname);
            info.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialog = new ProgressDialog(c);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage(progressDialog.getContext().getResources().getString(R.string.sjekker_brukernavn));
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try{
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("check_username", info.toString()));
            JSONObject json = new JSONParser().get_jsonobject("POST", params, null);
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

        return 0;
    }

    @Override
    protected void onPostExecute(Integer result) {
        progressDialog.hide();
        if(result == -1) {
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.brukernavn_opptatt), Toast.LENGTH_SHORT).show();
        } else if(result == 1) {
            try {
                Bruker.get().setBrukernavn(info.getString("username"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(progressDialog.getContext(), Register4Activity.class);
            progressDialog.getContext().startActivity(intent);
        } else {
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.ingen_kontakt_server), Toast.LENGTH_LONG).show();
        }
    }
}
