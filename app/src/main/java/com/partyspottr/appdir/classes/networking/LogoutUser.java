package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.ui.MainActivity;
import com.partyspottr.appdir.ui.SplashActivity;
import com.partyspottr.appdir.ui.mainfragments.chatfragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranarrr on 09-Feb-18.
 *
 * @author Ranarrr
 */

public class LogoutUser extends AsyncTask<Void, Void, Integer> {

    private ProgressDialog progressDialog;
    private JSONObject info;

    public LogoutUser(Activity activity) {
        if(!Bruker.get().isLoggetpa()) {
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            cancel(true);
        }

        if(SplashActivity.mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            cancel(true);
        }

        try {
            info = new JSONObject();
            info.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
            info.put("username", Bruker.get().getBrukernavn());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog = new ProgressDialog(activity);
        progressDialog.setOwnerActivity(activity);
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Logging out.."); // TODO: fix translation
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("logout_user", info.toString()));
            JSONObject json = new JSONParser().get_jsonobject("POST", params, null);
            if(json != null) {
                if(json.getInt("success") == 1) {
                    if(chatfragment.valueEventListener != null && chatfragment.ref != null)
                        chatfragment.ref.removeEventListener(chatfragment.valueEventListener);

                    if(SplashActivity.mAuth.getCurrentUser() != null)
                        SplashActivity.mAuth.signOut();

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
            Bruker.get().setLoggetpa(false);
            Bruker.get().setPassord("");
            Bruker.get().LagreBruker();

            Intent intent = new Intent(progressDialog.getContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            progressDialog.getContext().startActivity(intent);

            progressDialog.dismiss();

            if(progressDialog.getOwnerActivity() != null)
                progressDialog.getOwnerActivity().finish();
        } else if(integer == -1) {
            Toast.makeText(progressDialog.getContext(), "", Toast.LENGTH_SHORT).show(); // TODO: Fix translation
        } else {
            Toast.makeText(progressDialog.getContext(), "Failed to logout.", Toast.LENGTH_SHORT).show(); // TODO: Fix translation
        }
    }
}
