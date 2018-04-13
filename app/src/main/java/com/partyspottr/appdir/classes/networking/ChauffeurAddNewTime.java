package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Base64;

import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.classes.Bruker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChauffeurAddNewTime extends AsyncTask<Void, Void, Integer> {
    private ProgressDialog progressDialog;
    private JSONObject info;

    public ChauffeurAddNewTime(Activity activity, long timefrom, long timeto) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setOwnerActivity(activity);

        try {
            info = new JSONObject();
            info.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
            info.put("timefrom", new Date().getTime());
            info.put("timeto", timeto);
            info.put("brukernavnElem", Bruker.get().getBrukernavn());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPreExecute() {
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Adding new time.."); // TODO : Fix translation
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("add_new_time", info.toString()));
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
            try {
                Bruker.get().getChauffeur().setChauffeur_time_from(info.getLong("timefrom"));
                Bruker.get().getChauffeur().setChauffeur_time_to(info.getLong("timeto"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Bruker.get().getChauffeur().setChauffeur_time_from(0);
            Bruker.get().getChauffeur().setChauffeur_time_to(0);
        }

        Bruker.get().getChauffeur().LagreChauffeur();
    }
}
