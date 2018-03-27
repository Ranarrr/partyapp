package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.partyspottr.appdir.ui.MainActivity;
import com.partyspottr.appdir.ui.ProfilActivity;
import com.partyspottr.appdir.ui.SplashActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranarrr on 27-Jan-18.
 *
 * @author Ranarrr
 */

public class LoginUser extends AsyncTask<Void, Void, Integer> {

    private ProgressDialog progressDialog;
    private JSONObject info;

    public LoginUser(Activity activity, String user, String pass) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setOwnerActivity(activity);
        try {
            info = new JSONObject();
            info.put("username", user);
            info.put("pass", pass);
            info.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        if(progressDialog.getOwnerActivity() instanceof MainActivity) {
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage(progressDialog.getContext().getResources().getString(R.string.logger_inn));
            progressDialog.show();
        }
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("login_user", info.toString()));
            JSONObject json = new JSONParser().get_jsonobject("POST", params, null);
            if(json != null) {
                if(json.getInt("success") == 1) {
                    return 1;
                } else if(json.getInt("success") == 2) {
                    return 2;
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
            try {
                Bruker.get().setBrukernavn(info.getString("username"));
                Bruker.get().setPassord(info.getString("pass"));
                Bruker.get().LagreBruker();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(progressDialog.getOwnerActivity() != null) {
                Intent intent = new Intent(progressDialog.getOwnerActivity(), ProfilActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                progressDialog.getOwnerActivity().startActivity(intent);

                getUser getuser = new getUser(progressDialog.getOwnerActivity());
                getuser.execute();
            }

            progressDialog.dismiss();
        } else if(integer == 2) {
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.velkommen_admin), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(progressDialog.getContext(), ProfilActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            progressDialog.getContext().startActivity(intent);

            /*if(progressDialog.getOwnerActivity() != null)
                progressDialog.getOwnerActivity().finish();*/

            progressDialog.dismiss();
        } else if(integer == -1) { // wrong password
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.feil_passord), Toast.LENGTH_SHORT).show();
            Bruker.get().setBrukernavn("");
            Bruker.get().setPassord("");
        } else {
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.tilkoblingsfeil), Toast.LENGTH_SHORT).show();
        }
        progressDialog.hide();
    }
}