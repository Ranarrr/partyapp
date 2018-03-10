package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.classes.adapters.EventAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Ranarrr on 11-Feb-18.
 */

public class GetAllEvents extends AsyncTask<Void, Void, Integer> {

    private ProgressDialog progressDialog;

    public GetAllEvents(Context c, Activity activity) {
        progressDialog = new ProgressDialog(c);
        progressDialog.setOwnerActivity(activity);
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("get_all_events", BuildConfig.JSONParser_Socket));
            JSONArray json = new JSONParser().get_jsonarray("POST", params, null);
            if(json != null) {
                if(json.getJSONObject(json.length() - 1).getInt("success") == 1) {
                    json.remove(json.length() - 1);
                    Bruker.get().ParseEvents(json);
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
        if(progressDialog.getOwnerActivity() != null) {
            SwipeRefreshLayout swipeRefreshLayout = progressDialog.getOwnerActivity().findViewById(R.id.swipe_layout_events);
            ListView listView = progressDialog.getOwnerActivity().findViewById(R.id.lvalle_eventer);
            listView.setAdapter(new EventAdapter(progressDialog.getOwnerActivity(), Bruker.get().getListOfEvents()));
            if(swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }

        if(integer == 0) {
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.tilkoblingsfeil), Toast.LENGTH_SHORT).show();
        } else if(integer == -1) {
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.hente_event_mislyktes), Toast.LENGTH_SHORT).show();
        }
    }
}