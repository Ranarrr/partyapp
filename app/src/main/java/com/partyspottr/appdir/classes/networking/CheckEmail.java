package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.Base64;
import android.widget.EditText;
import android.widget.Toast;

import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.ui.SplashActivity;
import com.partyspottr.appdir.ui.registerui.Register3Activity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranarrr on 05-Mar-18.
 *
 * @author Ranarrr
 */

public class CheckEmail extends AsyncTask<Void, Void, Integer> {
    private JSONObject info;
    private ProgressDialog progressDialog;

    public CheckEmail(Activity activity, String e_mail) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setOwnerActivity(activity);
        try {
            info = new JSONObject();
            info.put("email", e_mail);
            info.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Checking email.."); // TODO: fix translation
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("check_email", info.toString()));
            JSONObject json = new JSONParser().get_jsonobject(params);
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
    protected void onPostExecute(Integer integer) {
        if(integer == -1) {
            Toast.makeText(progressDialog.getContext(), "This email is already in use!", Toast.LENGTH_SHORT).show(); // TODO: Fix translation

            if(progressDialog.getOwnerActivity() != null) {
                EditText emailtext = progressDialog.getOwnerActivity().findViewById(R.id.emailText);
                ViewCompat.setBackgroundTintList(emailtext, ContextCompat.getColorStateList(progressDialog.getContext(), R.color.redtint));
            }
        } else if(integer == 1) {
            try {
                Bruker.get().setEmail(info.getString("email"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(progressDialog.getOwnerActivity() != null) {
                EditText emailtext = progressDialog.getOwnerActivity().findViewById(R.id.emailText);
                ViewCompat.setBackgroundTintList(emailtext, ContextCompat.getColorStateList(progressDialog.getContext(), R.color.greentint));

                Intent intent = new Intent(progressDialog.getOwnerActivity(), Register3Activity.class);
                progressDialog.getOwnerActivity().startActivity(intent);
            }
        } else {
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.tilkoblingsfeil), Toast.LENGTH_SHORT).show();
        }

        progressDialog.hide();
    }
}
