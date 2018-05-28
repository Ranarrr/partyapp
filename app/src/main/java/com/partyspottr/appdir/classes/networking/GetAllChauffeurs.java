package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.widget.ListView;
import android.widget.Toast;

import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Chauffeur;
import com.partyspottr.appdir.classes.adapters.ChauffeurAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class GetAllChauffeurs extends AsyncTask<Void, Void, Integer> {
    ProgressDialog progressDialog;
    private String error_msg;

    public GetAllChauffeurs(Activity activity) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setOwnerActivity(activity);



    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("get_all_chauffeurs", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT)));
            JSONArray json = new JSONParser().get_jsonarray("POST", params, null);
            if(json != null) {
                if(json.getJSONObject(json.length() - 1).getInt("success") == 1) {
                    json.remove(json.length() - 1);
                    Bruker.get().ParseChauffeurs(json);
                    return 1;
                } else {
                    error_msg = json.getJSONObject(json.length() - 1).getString("error_msg");
                    json.remove(json.length() - 1);
                    Bruker.get().setListchauffeurs(new ArrayList<Chauffeur>());
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
        if(progressDialog.getOwnerActivity() != null) {
            ListView lv_chauffeurs = progressDialog.getOwnerActivity().findViewById(R.id.lv_chauffeurs);
            SwipeRefreshLayout swipeRefreshLayout = progressDialog.getOwnerActivity().findViewById(R.id.swipe_chauffeurs);

            if(lv_chauffeurs != null)
                lv_chauffeurs.setAdapter(new ChauffeurAdapter(progressDialog.getOwnerActivity(), Bruker.get().getListchauffeurs()));

            if(swipeRefreshLayout != null)
                swipeRefreshLayout.setRefreshing(false);
        }

        if(integer == 0) {
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.tilkoblingsfeil), Toast.LENGTH_SHORT).show();
        } else if(integer == -1) {
            if(error_msg.equals("No chauffeurs.")) {
                Toast.makeText(progressDialog.getContext(), "There are no chauffeurs.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(progressDialog.getContext(), "Could not retrieve chauffeurs", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
