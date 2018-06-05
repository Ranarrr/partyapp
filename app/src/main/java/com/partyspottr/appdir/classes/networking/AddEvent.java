package com.partyspottr.appdir.classes.networking;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Event;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranarrr on 13-Feb-18.
 *
 * @author Ranarrr
 */

public class AddEvent extends AsyncTask<Void, Void, Integer> {
    private Event eventToUse;
    private ProgressDialog progressDialog;
    private File bitmap;
    private Dialog dialog;

    AddEvent(Dialog dilog, ProgressDialog pD, Event event, File bmp) {
        progressDialog = pD;
        dialog = dilog;
        eventToUse = event;
        eventToUse.setHasimage(bmp != null);
        bitmap = bmp;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("add_event", eventToUse.EventToJSON()));
            JSONObject json = new JSONParser().get_jsonobject(params);

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
        if(integer == 1) {
            Bruker.get().addToMyEvents(eventToUse);
            if(bitmap != null) {
                UploadImage compressImage = new UploadImage(progressDialog, eventToUse, bitmap, null);
                compressImage.execute();
            } else {
                Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.event_lagt_til), Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }

            if(dialog != null) {
                dialog.onBackPressed();
            }

        } else if(integer == 0) {
            progressDialog.hide();
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.tilkoblingsfeil), Toast.LENGTH_SHORT).show();
        } else if(integer == -1) {
            progressDialog.hide();
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.legge_til_event_mislyktes), Toast.LENGTH_SHORT).show();
        }
    }
}
