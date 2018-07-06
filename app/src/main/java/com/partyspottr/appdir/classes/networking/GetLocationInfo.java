package com.partyspottr.appdir.classes.networking;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Event;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ranarrr on 22-Feb-18.
 *
 * @author Ranarrr
 */

public class GetLocationInfo extends AsyncTask<Void, Void, Integer> {
    private String addressToUse;
    private String PostalCode;
    private Event eventToUse;
    ProgressDialog progressDialog;
    private Dialog dilog;

    public GetLocationInfo(Dialog dialog, String address, String postal_code) {
        if(dialog.getOwnerActivity() != null) {
            progressDialog = new ProgressDialog(dialog.getContext(), R.style.mydatepickerdialog);
            progressDialog.setOwnerActivity(dialog.getOwnerActivity());
            dilog = dialog;
        }

        addressToUse = address;
        PostalCode = postal_code;
        eventToUse = new Event();
    }

    @Override
    protected void onPreExecute() {
        if(PostalCode == null || addressToUse.isEmpty())
            cancel(true);

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
            if (dilog != null)
                ((TextView) dilog.findViewById(R.id.by_textview)).setText(eventToUse.getTown());
        } else
            if(dilog != null)
                ((TextView) dilog.findViewById(R.id.by_textview)).setText(progressDialog.getContext().getResources().getString(R.string.town));
    }
}
