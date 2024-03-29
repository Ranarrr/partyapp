package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.ui.MainActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranarrr on 01-Jun-18.
 *
 * @author Ranarrr
 */

public class DeleteUser extends AsyncTask<Void, Void, Integer> {
    ProgressDialog progressDialog;
    private JSONObject info;
    private String error_msg;

    public DeleteUser(Activity activity, String pass) {
        progressDialog  = new ProgressDialog(activity, R.style.mydatepickerdialog);
        progressDialog.setOwnerActivity(activity);

        try {
            info = new JSONObject();
            info.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
            info.put("pass", pass);
            info.put("user", Bruker.get().getBrukernavn());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Deleting user..");
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("delete_user", info.toString()));
            JSONObject json = new JSONParser().get_jsonobject(params);
            if(json != null) {
                if(json.getInt("success") == 1) {
                    return 1;
                } else {
                    error_msg = json.getString("error_msg");
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
        progressDialog.hide();

        if(integer == 1) {
            if(progressDialog.getOwnerActivity() != null) {
                Bruker.get().setLoggetpa(false);
                Bruker.get().setBrukernavn("");
                Bruker.get().setPassord("");
                Bruker.get().setFornavn("");
                Bruker.get().setEtternavn("");

                Bruker.get().DeleteBruker();
                Intent intent = new Intent(progressDialog.getOwnerActivity(), MainActivity.class);
                progressDialog.getOwnerActivity().startActivity(intent);
            } else {
                Bruker.get().setLoggetpa(false);
                Bruker.get().setBrukernavn("");
                Bruker.get().setPassord("");
                Bruker.get().setFornavn("");
                Bruker.get().setEtternavn("");
                Bruker.get().DeleteBruker();
                System.exit(0);
            }
        } else {
            switch (error_msg) {
                case "Wrong password.":
                    Toast.makeText(progressDialog.getContext(), "Wrong password. Please try again.", Toast.LENGTH_LONG).show();
                    break;
                case "User not found.":
                    Toast.makeText(progressDialog.getContext(), "Failed to delete account, please send us an email at support@partyspottr.com.", Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(progressDialog.getContext(), "Failed to delete account, please try again later.", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
