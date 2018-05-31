package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.ui.SplashActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranarrr on 31-Jan-18.
 *
 * @author Ranarrr
 */

public class getUser extends AsyncTask<Void, Void, Integer> {
    private ProgressDialog progressDialog;
    private JSONObject info;

    getUser(Activity activity) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setOwnerActivity(activity);
        try {
            info = new JSONObject();
            info.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
            info.put("username", Bruker.get().getBrukernavn());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try{
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("get_user", info.toString()));
            JSONObject json = new JSONParser().get_jsonobject(params);
            if(json != null) {
                if(json.getInt("success") == 1) {
                    Bruker.get().JSONToBruker(json, true);
                    Bruker.get().populateChauffeur(progressDialog.getOwnerActivity());
                    SplashActivity.mAuth.signInWithEmailAndPassword(Bruker.get().getEmail(), Bruker.get().getPassord())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.velkommen) + " " + Bruker.get().getBrukernavn() + "!", Toast.LENGTH_SHORT).show();
                                        Bruker.get().setLoggetpa(true);
                                    } else {
                                        Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.tilkoblingsfeil), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    return 1;
                } else {
                    return 0;
                }
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if(integer == 0) {
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.hente_bruker_mislykkes), Toast.LENGTH_SHORT).show();
            System.exit(0);
        }
    }
}
