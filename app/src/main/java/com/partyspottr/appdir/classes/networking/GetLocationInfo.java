package com.partyspottr.appdir.classes.networking;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Event;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Ranarrr on 22-Feb-18.
 *
 * @author Ranarrr
 */

public class GetLocationInfo extends AsyncTask<Void, Void, Integer> {

    private String addressToUse;
    private int PostalCode;
    private File bitmap;
    private Event eventToUse;
    ProgressDialog progressDialog;
    private boolean addEvent;
    private Dialog dilog;

    public GetLocationInfo(Dialog dialog, String address, int postal_code, Event event, File bmp, boolean addevent) {
        if(dialog.getOwnerActivity() != null) {
            progressDialog = new ProgressDialog(dialog.getContext());
            progressDialog.setOwnerActivity(dialog.getOwnerActivity());
            dilog = dialog;
        }

        bitmap = bmp;
        addEvent = addevent;
        addressToUse = address;
        PostalCode = postal_code;
        eventToUse = event;
    }

    @Override
    protected void onPreExecute() {
        if(addEvent) {
            progressDialog.setMessage(progressDialog.getContext().getResources().getString(R.string.legger_til_event));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        } else {
            if(PostalCode == 0 || addressToUse.isEmpty()) {
                cancel(true);
            }
        }

        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        JSONObject json = GoogleAPIRequest.makeHTTPReq(progressDialog.getContext(), addressToUse, PostalCode);
        try {
            if(json.getString("status").equals("OK")) {
                eventToUse.ParseFromGoogleReq(json);
                return 1;
            } else {
                return -1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if(integer == 1) {
            if(addEvent) {
                AddEvent addEvent = new AddEvent(progressDialog, eventToUse, bitmap);
                addEvent.execute();
            } else {
                if(dilog != null) {
                    ((TextView) dilog.findViewById(R.id.by_textview)).setText(eventToUse.getTown());
                }

            }
        } else {
            if(addEvent)
                Toast.makeText(progressDialog.getContext(), "Failed to retreive location info for the event!", Toast.LENGTH_LONG).show();

            if(dilog != null)
                ((TextView) dilog.findViewById(R.id.by_textview)).setText("By"); // TODO : translation
        }
    }
}
