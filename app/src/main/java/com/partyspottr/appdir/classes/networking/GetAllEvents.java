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
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.classes.adapters.EventAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Ranarrr on 11-Feb-18.
 */

public class GetAllEvents extends AsyncTask<Void, Void, Integer> {

    private ProgressDialog progressDialog;
    private String error_msg;

    public GetAllEvents(Activity activity) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setOwnerActivity(activity);
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("get_all_events", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT)));
            JSONArray json = new JSONParser().get_jsonarray(params);
            if(json != null) {
                if(json.getJSONObject(json.length() - 1).getInt("success") == 1) {
                    json.remove(json.length() - 1);
                    Bruker.get().ParseEvents(json);
                    return 1;
                } else {
                    error_msg = json.getJSONObject(json.length() - 1).getString("error_msg");
                    json.remove(json.length() - 1);
                    Bruker.get().setListOfEvents(new ArrayList<Event>());
                    Bruker.get().setListOfMyEvents(new ArrayList<Event>());
                    return -1;
                }
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if(progressDialog.getOwnerActivity() != null) {
            ListView allevents = progressDialog.getOwnerActivity().findViewById(R.id.lvalle_eventer);
            SwipeRefreshLayout swipeRefreshLayout = progressDialog.getOwnerActivity().findViewById(R.id.swipe_layout_events);

            if(allevents != null)
                allevents.setAdapter(new EventAdapter(progressDialog.getOwnerActivity(), Bruker.get().getListOfEvents()));

            ListView myevents = progressDialog.getOwnerActivity().findViewById(R.id.lvmine_eventer);
            if(myevents != null)
                myevents.setAdapter(new EventAdapter(progressDialog.getOwnerActivity(), Bruker.get().getListOfMyEvents()));
            if(swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
            }

            if(integer == 0) {
                Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.tilkoblingsfeil), Toast.LENGTH_SHORT).show();
            } else if(integer == -1) {
                if(error_msg.equals("No events.")) {
                    Toast.makeText(progressDialog.getContext(), "There are no events.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.hente_event_mislyktes), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}