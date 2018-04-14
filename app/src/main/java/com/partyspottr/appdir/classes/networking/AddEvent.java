package com.partyspottr.appdir.classes.networking;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Event;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Ranarrr on 13-Feb-18.
 *
 * @author Ranarrr
 */

public class AddEvent extends AsyncTask<Void, Void, Integer> {
    private JSONObject eventToAdd;
    private Event eventToUse;
    private ProgressDialog progressDialog;
    private File bitmap;
    private Dialog dialog;

    AddEvent(Dialog dilog, ProgressDialog pD, Event event, File bmp) {
        progressDialog = pD;
        dialog = dilog;
        eventToUse = event;
        bitmap = bmp;
        try {
            eventToAdd = new JSONObject(new Gson().toJson(event));
            eventToAdd.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
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
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("add_event", eventToAdd.toString()));
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

        return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        progressDialog.hide();
        if(integer == 1) {
            Bruker.get().addToMyEvents(eventToUse);
            if(bitmap != null) {
                UploadImage compressImage = new UploadImage(progressDialog, eventToUse, bitmap);
                compressImage.execute();
            } else {
                Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.event_lagt_til), Toast.LENGTH_SHORT).show();
            }

            if(dialog != null) {
                dialog.onBackPressed();
            }

        } else if(integer == 0) {
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.tilkoblingsfeil), Toast.LENGTH_SHORT).show();
        } else if(integer == -1) {
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.legge_til_event_mislyktes), Toast.LENGTH_SHORT).show();
        }
    }
}
