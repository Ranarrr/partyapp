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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private String password;

    public LoginUser(Activity activity, String user, String pass) {
        progressDialog = new ProgressDialog(activity, R.style.mydatepickerdialog);
        progressDialog.setOwnerActivity(activity);
        password = pass;

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
            JSONObject json = new JSONParser().get_jsonobject(params);
            if(json != null) {
                if(json.getInt("success") == 1) {
                    SplashActivity.hasSwitched = true;

                    try {
                        if(!info.getString("username").equalsIgnoreCase(Bruker.get().getBrukernavn())) {
                            Bruker.get().DeleteBruker();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Bruker.get().setEmail(json.getString("email"));
                    Bruker.get().setBrukernavn(info.getString("username"));
                    Bruker.get().setPassord(info.getString("pass"));

                    SplashActivity.mAuth.signInWithEmailAndPassword(json.getString("email"), password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.velkommen) + " " + Bruker.get().getBrukernavn().toLowerCase() + "!", Toast.LENGTH_SHORT).show();

                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(Bruker.get().getBrukernavn().toLowerCase());
                                        ref.child("loggedon").setValue(true);
                                        Bruker.get().setLoggetpa(true);
                                    } else
                                        Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.tilkoblingsfeil), Toast.LENGTH_SHORT).show();
                                }
                            });

                    return 1;
                } else if(json.getInt("success") == 2) {
                    Bruker.get().setLoggetpa(true);

                    SplashActivity.hasSwitched = true;
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
            if(progressDialog.getOwnerActivity() != null) {
                Intent intent = new Intent(progressDialog.getOwnerActivity(), ProfilActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                progressDialog.getOwnerActivity().startActivity(intent);
                progressDialog.getOwnerActivity().finish();
            }

            progressDialog.dismiss();
        } else if(integer == 2) {
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.velkommen_admin), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(progressDialog.getContext(), ProfilActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            progressDialog.getContext().startActivity(intent);

            if(progressDialog.getOwnerActivity() != null)
                progressDialog.getOwnerActivity().finish();

            progressDialog.dismiss();
        } else if(integer == -1) { // wrong password
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.feil_passord), Toast.LENGTH_SHORT).show();
            Bruker.get().setPassord("");
        } else
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.tilkoblingsfeil), Toast.LENGTH_SHORT).show();

        progressDialog.hide();
    }
}